<?php

namespace App\Entity;

use App\Repository\PlanningActivityRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PlanningActivityRepository::class)]
#[ORM\Table(name: 'planningactivity')]
class PlanningActivity
{
    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Activity::class, inversedBy: 'plannings')]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity', nullable: false)]
    private ?Activity $activity = null;

    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Sallesportif::class, inversedBy: 'plannings')]
    #[ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle', nullable: false)]
    private ?Sallesportif $salle = null;

    #[ORM\Column(name: 'capacityMax', type: 'integer')]
    private ?int $capacityMax = null;

    #[ORM\Column(name: 'nombreinscription', type: 'integer')]
    private ?int $nombreInscription = 0;

    // Getters and Setters

   

    public function getActivity(): ?Activity
    {
        return $this->activity;
    }

    public function setActivity(?Activity $activity): self
    {
        $this->activity = $activity;
        return $this;
    }

    public function getSalle(): ?Sallesportif
    {
        return $this->salle;
    }

    public function setSalle(?Sallesportif $salle): self
    {
        $this->salle = $salle;
        return $this;
    }

    public function getCapacityMax(): ?int
    {
        return $this->capacityMax;
    }

    public function setCapacityMax(int $capacityMax): self
    {
        $this->capacityMax = $capacityMax;
        return $this;
    }

    public function getNombreInscription(): ?int
    {
        return $this->nombreInscription;
    }

    public function setNombreInscription(int $nombreInscription): self
    {
        $this->nombreInscription = $nombreInscription;
        return $this;
    }

    // Méthode utilitaire
    public function __toString(): string
    {
        return $this->activity?->getNomActivity() . ' - ' . $this->salle?->getNomSalle();
    }
}