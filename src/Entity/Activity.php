<?php

namespace App\Entity;

use App\Repository\ActivityRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ActivityRepository::class)]
class Activity
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    private ?string $nomActivity = null;

    #[ORM\Column(length: 255)]
    private ?string $IconActivity = null;

    #[ORM\Column]
    private ?int $categorieActivity = null;

    /**
     * @var Collection<int, Seance>
     */
    #[ORM\OneToMany(targetEntity: Seance::class, mappedBy: 'activite_id', cascade: ['remove'], orphanRemoval: true)]
    private Collection $seances;

    public function __construct()
    {
        $this->seances = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNomActivity(): ?string
    {
        return $this->nomActivity;
    }

    public function setNomActivity(string $nomActivity): static
    {
        $this->nomActivity = $nomActivity;

        return $this;
    }

    public function getIconActivity(): ?string
    {
        return $this->IconActivity;
    }

    public function setIconActivity(string $IconActivity): static
    {
        $this->IconActivity = $IconActivity;

        return $this;
    }

    public function getCategorieActivity(): ?int
    {
        return $this->categorieActivity;
    }

    public function setCategorieActivity(int $categorieActivity): static
    {
        $this->categorieActivity = $categorieActivity;

        return $this;
    }

    /**
     * @return Collection<int, Seance>
     */
    public function getSeances(): Collection
    {
        return $this->seances;
    }

    public function addSeance(Seance $seance): static
    {
        if (!$this->seances->contains($seance)) {
            $this->seances->add($seance);
            $seance->setActiviteId($this);
        }

        return $this;
    }

    public function removeSeance(Seance $seance): static
    {
        if ($this->seances->removeElement($seance)) {
            // set the owning side to null (unless already changed)
            if ($seance->getActiviteId() === $this) {
                $seance->setActiviteId(null);
            }
        }

        return $this;
    }
}
