<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ActivityRepository;

#[ORM\Entity(repositoryClass: ActivityRepository::class)]
#[ORM\Table(name: 'activity')]
class Activity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $idActivity = null;

    public function getIdActivity(): ?int
    {
        return $this->idActivity;
    }

    public function setIdActivity(int $idActivity): self
    {
        $this->idActivity = $idActivity;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nomActivity = null;

    public function getNomActivity(): ?string
    {
        return $this->nomActivity;
    }

    public function setNomActivity(string $nomActivity): self
    {
        $this->nomActivity = $nomActivity;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $IconActivity = null;

    public function getIconActivity(): ?string
    {
        return $this->IconActivity;
    }

    public function setIconActivity(string $IconActivity): self
    {
        $this->IconActivity = $IconActivity;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $categorieActivity = null;

    public function getCategorieActivity(): ?string
    {
        return $this->categorieActivity;
    }

    public function setCategorieActivity(string $categorieActivity): self
    {
        $this->categorieActivity = $categorieActivity;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Seance::class, mappedBy: 'activity')]
    private Collection $seances;

    /**
     * @return Collection<int, Seance>
     */
    public function getSeances(): Collection
    {
        if (!$this->seances instanceof Collection) {
            $this->seances = new ArrayCollection();
        }
        return $this->seances;
    }

    public function addSeance(Seance $seance): self
    {
        if (!$this->getSeances()->contains($seance)) {
            $this->getSeances()->add($seance);
        }
        return $this;
    }

    public function removeSeance(Seance $seance): self
    {
        $this->getSeances()->removeElement($seance);
        return $this;
    }

    #[ORM\ManyToMany(targetEntity: Sallesportif::class, inversedBy: 'activitys')]
    #[ORM\JoinTable(
        name: 'planningactivity',
        joinColumns: [
            new ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity')
        ],
        inverseJoinColumns: [
            new ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle')
        ]
    )]
    private Collection $sallesportifs;

    public function __construct()
    {
        $this->seances = new ArrayCollection();
        $this->sallesportifs = new ArrayCollection();
    }

    /**
     * @return Collection<int, Sallesportif>
     */
    public function getSallesportifs(): Collection
    {
        if (!$this->sallesportifs instanceof Collection) {
            $this->sallesportifs = new ArrayCollection();
        }
        return $this->sallesportifs;
    }

    public function addSallesportif(Sallesportif $sallesportif): self
    {
        if (!$this->getSallesportifs()->contains($sallesportif)) {
            $this->getSallesportifs()->add($sallesportif);
        }
        return $this;
    }

    public function removeSallesportif(Sallesportif $sallesportif): self
    {
        $this->getSallesportifs()->removeElement($sallesportif);
        return $this;
    }
}
