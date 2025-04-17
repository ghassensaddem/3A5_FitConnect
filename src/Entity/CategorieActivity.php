<?php

namespace App\Entity;

use App\Repository\CategorieActivityRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CategorieActivityRepository::class)]
#[ORM\Table(name: 'categorieactivity')]
class CategorieActivity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'idCategorie', type: 'integer')]
    private ?int $idCategorie = null;

    #[ORM\Column(name: 'nomCategorie', type: 'string', length: 30)]
    private ?string $nomCategorie = null;

    #[ORM\Column(type: 'string', length: 250)]
    private ?string $description = null;

    #[ORM\OneToMany(targetEntity: 'App\Entity\Activity', mappedBy: 'categorieActivity')]
    private Collection $activities;

    public function __construct()
    {
        $this->activities = new ArrayCollection();
    }

    // Getters et Setters existants
    public function getIdCategorie(): ?int
    {
        return $this->idCategorie;
    }

    public function getNomCategorie(): ?string
    {
        return $this->nomCategorie;
    }

    public function setNomCategorie(string $nomCategorie): self
    {
        $this->nomCategorie = $nomCategorie;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }

    // Méthodes pour la relation avec Activity
    public function getActivities(): Collection
    {
        return $this->activities;
    }

    public function addActivity(Activity $activity): self
    {
        if (!$this->activities->contains($activity)) {
            $this->activities[] = $activity;
            $activity->setCategorieActivity($this);
        }
        return $this;
    }

    public function removeActivity(Activity $activity): self
    {
        if ($this->activities->removeElement($activity)) {
            // set the owning side to null (unless already changed)
            if ($activity->getCategorieActivity() === $this) {
                $activity->setCategorieActivity(null);
            }
        }
        return $this;
    }

    // Méthode pour affichage simple
    public function __toString(): string
    {
        return $this->nomCategorie ?? '';
    }
}