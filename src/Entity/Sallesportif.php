<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\SallesportifRepository;

#[ORM\Entity(repositoryClass: SallesportifRepository::class)]
#[ORM\Table(name: 'sallesportif')]
class Sallesportif
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $idSalle = null;

    public function getIdSalle(): ?int
    {
        return $this->idSalle;
    }

    public function setIdSalle(int $idSalle): self
    {
        $this->idSalle = $idSalle;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nomSalle = null;

    public function getNomSalle(): ?string
    {
        return $this->nomSalle;
    }

    public function setNomSalle(string $nomSalle): self
    {
        $this->nomSalle = $nomSalle;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $addresseSalle = null;

    public function getAddresseSalle(): ?string
    {
        return $this->addresseSalle;
    }

    public function setAddresseSalle(string $addresseSalle): self
    {
        $this->addresseSalle = $addresseSalle;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: false)]
    private ?string $HoraireOuverture = null;

    public function getHoraireOuverture(): ?string
    {
        return $this->HoraireOuverture;
    }

    public function setHoraireOuverture(string $HoraireOuverture): self
    {
        $this->HoraireOuverture = $HoraireOuverture;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: false)]
    private ?string $HoraireFermeture = null;

    public function getHoraireFermeture(): ?string
    {
        return $this->HoraireFermeture;
    }

    public function setHoraireFermeture(string $HoraireFermeture): self
    {
        $this->HoraireFermeture = $HoraireFermeture;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $Capacity = null;

    public function getCapacity(): ?int
    {
        return $this->Capacity;
    }

    public function setCapacity(int $Capacity): self
    {
        $this->Capacity = $Capacity;
        return $this;
    }

    #[ORM\ManyToMany(targetEntity: Activity::class, inversedBy: 'sallesportifs')]
    #[ORM\JoinTable(
        name: 'planningactivity',
        joinColumns: [
            new ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle')
        ],
        inverseJoinColumns: [
            new ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity')
        ]
    )]
    private Collection $activitys;

    public function __construct()
    {
        $this->activitys = new ArrayCollection();
    }

    /**
     * @return Collection<int, Activity>
     */
    public function getActivitys(): Collection
    {
        if (!$this->activitys instanceof Collection) {
            $this->activitys = new ArrayCollection();
        }
        return $this->activitys;
    }

    public function addActivity(Activity $activity): self
    {
        if (!$this->getActivitys()->contains($activity)) {
            $this->getActivitys()->add($activity);
        }
        return $this;
    }

    public function removeActivity(Activity $activity): self
    {
        $this->getActivitys()->removeElement($activity);
        return $this;
    }
}
