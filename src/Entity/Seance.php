<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\SeanceRepository;

#[ORM\Entity(repositoryClass: SeanceRepository::class)]
#[ORM\Table(name: 'seance')]
class Seance
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    #[ORM\Column(type: 'date', nullable: false)]
    private ?\DateTimeInterface $date = null;

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(\DateTimeInterface $date): self
    {
        $this->date = $date;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: false)]
    private ?string $horaire = null;

    public function getHoraire(): ?string
    {
        return $this->horaire;
    }

    public function setHoraire(string $horaire): self
    {
        $this->horaire = $horaire;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $lieu = null;

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(string $lieu): self
    {
        $this->lieu = $lieu;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Programme::class, inversedBy: 'seances')]
    #[ORM\JoinColumn(name: 'programme_id', referencedColumnName: 'id')]
    private ?Programme $programme = null;

    public function getProgramme(): ?Programme
    {
        return $this->programme;
    }

    public function setProgramme(?Programme $programme): self
    {
        $this->programme = $programme;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Activity::class, inversedBy: 'seances')]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity')]
    private ?Activity $activity = null;

    public function getActivity(): ?Activity
    {
        return $this->activity;
    }

    public function setActivity(?Activity $activity): self
    {
        $this->activity = $activity;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Avi::class, mappedBy: 'seance')]
    private Collection $avis;

    public function __construct()
    {
        $this->avis = new ArrayCollection();
    }

    /**
     * @return Collection<int, Avi>
     */
    public function getAvis(): Collection
    {
        if (!$this->avis instanceof Collection) {
            $this->avis = new ArrayCollection();
        }
        return $this->avis;
    }

    public function addAvi(Avi $avi): self
    {
        if (!$this->getAvis()->contains($avi)) {
            $this->getAvis()->add($avi);
        }
        return $this;
    }

    public function removeAvi(Avi $avi): self
    {
        $this->getAvis()->removeElement($avi);
        return $this;
    }

}
