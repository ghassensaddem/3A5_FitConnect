<?php

namespace App\Entity;

use App\Repository\SallesportifRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

#[ORM\Entity(repositoryClass: SallesportifRepository::class)]
class Sallesportif
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'idSalle', type: 'integer')]
    private ?int $idSalle = null;

    #[ORM\Column(name: 'nomSalle', length: 30)]
    private ?string $nomSalle = null;

    #[ORM\Column(name: 'addresseSalle', length: 250)]
    private ?string $addresseSalle = null;

    #[ORM\Column(name: 'HoraireOuverture', type: 'time')]
    private ?\DateTimeInterface $horaireOuverture = null;

    #[ORM\Column(name: 'HoraireFermeture', type: 'time')]
    private ?\DateTimeInterface $horaireFermeture = null;

    #[ORM\Column(name: 'Capacity', type: 'integer')]
    private ?int $capacity = null;

    #[ORM\OneToMany(mappedBy: 'salle', targetEntity: PlanningActivity::class)]
    private Collection $plannings;

    // Getters and setters

    public const ADDRESSES = [
        'Menzah 5',
        'Lac 2',
        'Lac 1',
        'Hammem Ghzez',
        'Kelibiia'
    ];

    public function getIdSalle(): ?int
    {
        return $this->idSalle;
    }

    public function getNomSalle(): ?string
    {
        return $this->nomSalle;
    }

    public function setNomSalle(string $nomSalle): self
    {
        $this->nomSalle = $nomSalle;

        return $this;
    }

    public function getAddresseSalle(): ?string
    {
        return $this->addresseSalle;
    }

    public function setAddresseSalle(string $addresseSalle): self
    {
        $this->addresseSalle = $addresseSalle;

        return $this;
    }

    public function getHoraireOuverture(): ?\DateTimeInterface
    {
        return $this->horaireOuverture;
    }

    public function setHoraireOuverture(\DateTimeInterface $horaireOuverture): self
    {
        $this->horaireOuverture = $horaireOuverture;

        return $this;
    }

    public function getHoraireFermeture(): ?\DateTimeInterface
    {
        return $this->horaireFermeture;
    }

    public function setHoraireFermeture(\DateTimeInterface $horaireFermeture): self
    {
        $this->horaireFermeture = $horaireFermeture;

        return $this;
    }

    public function getCapacity(): ?int
    {
        return $this->capacity;
    }

    public function setCapacity(int $capacity): self
    {
        $this->capacity = $capacity;

        return $this;
    }
}