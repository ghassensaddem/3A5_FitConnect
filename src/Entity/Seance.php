<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Activity;
use Doctrine\Common\Collections\Collection;
use App\Entity\Avis;

#[ORM\Entity]
class Seance
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Programme::class, inversedBy: "seances")]
    #[ORM\JoinColumn(name: 'programme_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Programme $programme_id;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $date;

    #[ORM\Column(type: "string", length: 5)]
    private string $horaire;

        #[ORM\ManyToOne(targetEntity: Activity::class, inversedBy: "seances")]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity', onDelete: 'CASCADE')]
    private Activity $idActivity;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getProgramme_id()
    {
        return $this->programme_id;
    }

    public function setProgramme_id($value)
    {
        $this->programme_id = $value;
    }

    public function getDate()
    {
        return $this->date;
    }

    public function setDate($value)
    {
        $this->date = $value;
    }

    public function getHoraire()
    {
        return $this->horaire;
    }

    public function setHoraire($value)
    {
        $this->horaire = $value;
    }

    public function getIdActivity()
    {
        return $this->idActivity;
    }

    public function setIdActivity($value)
    {
        $this->idActivity = $value;
    }

    #[ORM\OneToMany(mappedBy: "seanceid", targetEntity: Avis::class)]
    private Collection $aviss;

    public function __construct()
    {
        $this->aviss = new ArrayCollection();
    }

    public function getProgrammeId(): ?Programme
    {
        return $this->programme_id;
    }

    public function setProgrammeId(?Programme $programme_id): static
    {
        $this->programme_id = $programme_id;

        return $this;
    }

    /**
     * @return Collection<int, Avis>
     */
    public function getAviss(): Collection
    {
        return $this->aviss;
    }

    public function addAviss(Avis $aviss): static
    {
        if (!$this->aviss->contains($aviss)) {
            $this->aviss->add($aviss);
            $aviss->setSeanceid($this);
        }

        return $this;
    }

    public function removeAviss(Avis $aviss): static
    {
        if ($this->aviss->removeElement($aviss)) {
            // set the owning side to null (unless already changed)
            if ($aviss->getSeanceid() === $this) {
                $aviss->setSeanceid(null);
            }
        }

        return $this;
    }
}
