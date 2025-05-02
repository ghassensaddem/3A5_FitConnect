<?php

namespace App\Entity;

use App\Repository\ActivityRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

#[ORM\Entity(repositoryClass: ActivityRepository::class)]
#[ORM\Table(name: 'activity')]
class Activity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'idActivity', type: 'integer')]
    private ?int $idActivity = null;

    #[ORM\Column(name: 'nomActivity', type: 'string', length: 20)]
    private ?string $nomActivity = null;

    #[ORM\Column(name: 'IconActivity', type: 'string', length: 250)]
    private ?string $iconActivity = null;

    #[ORM\ManyToOne(targetEntity: CategorieActivity::class, inversedBy: 'activities')]
    #[ORM\JoinColumn(name: 'categorieActivity', referencedColumnName: 'idCategorie', nullable: false)]
    private ?CategorieActivity $categorieActivity = null;

    #[ORM\OneToMany(mappedBy: 'activity', targetEntity: PlanningActivity::class)]
    private Collection $plannings;

    // Getters et Setters

    public function getIdActivity(): ?int
    {
        return $this->idActivity;
    }

    public function getNom(): ?string
    {
        return $this->nomActivity;
    }

    public function getNomActivity(): ?string
    {
        return $this->nomActivity;
    }

    public function setNomActivity(string $nomActivity): self
    {
        $this->nomActivity = $nomActivity;
        return $this;
    }

    public function getIconActivity(): ?string
    {
        return $this->iconActivity;
    }

    public function setIconActivity(string $iconActivity): self
    {
        $this->iconActivity = $iconActivity;
        return $this;
    }

    public function getCategorieActivity(): ?CategorieActivity
    {
        return $this->categorieActivity;
    }

    public function setCategorieActivity(?CategorieActivity $categorieActivity): self
    {
        $this->categorieActivity = $categorieActivity;
        return $this;
    }

    public function __toString(): string
    {
        return $this->nomActivity ?? '';
    }

    public function getSallesAssociees(): array
    {
        return $this->plannings
            ->map(function($planning) {
                return $planning->getSalle();
            })
            ->toArray();
    }
}