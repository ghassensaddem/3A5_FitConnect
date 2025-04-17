<?php

namespace App\Entity;

use App\Repository\SeanceRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: SeanceRepository::class)]
class Seance
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotNull(message: "La date ne doit pas être vide.")]
    #[Assert\GreaterThanOrEqual("today", message: "La date ne peut pas être dans le passé.")]
    #[Assert\Type("\DateTimeInterface")]
    private ?\DateTimeInterface $date = null;

    #[ORM\Column(type: Types::TIME_MUTABLE)]
    #[Assert\NotBlank(message: "L'horaire ne doit pas être vide.")]
    private ?\DateTimeInterface $horaire = null;

    #[ORM\ManyToOne(inversedBy: 'seances')]
#[ORM\JoinColumn(name: 'programme_id', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
#[Assert\NotNull(message: "Le programme est requis.")]
private ?Programme $programme_id = null;


    #[ORM\ManyToOne(inversedBy: 'seances')]
    #[ORM\JoinColumn(nullable: false, name: 'activite_id', referencedColumnName: 'id',onDelete: 'CASCADE')]
    #[Assert\NotNull(message: "L'activité est requise.")]
    private ?Activity $activite_id = null;

    /**
     * @var Collection<int, Avis>
     */
    #[ORM\OneToMany(targetEntity: Avis::class, mappedBy: 'seanceid', cascade: ['remove'], orphanRemoval: true)]
    private Collection $avis;

    public function __construct()
    {
        $this->avis = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(?\DateTimeInterface $date): static
    {
        $this->date = $date;

        return $this;
    }

    public function getHoraire(): ?\DateTimeInterface
    {
        return $this->horaire;
    }

    public function setHoraire(?\DateTimeInterface $horaire): static
    {
        $this->horaire = $horaire;

        return $this;
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

    public function getActiviteId(): ?Activity
    {
        return $this->activite_id;
    }

    public function setActiviteId(?Activity $activite_id): static
    {
        $this->activite_id = $activite_id;

        return $this;
    }

    /**
     * @return Collection<int, Avis>
     */
    public function getAvis(): Collection
    {
        return $this->avis;
    }

    public function addAvi(Avis $avi): static
    {
        if (!$this->avis->contains($avi)) {
            $this->avis->add($avi);
            $avi->setSeanceid($this);
        }

        return $this;
    }

    public function removeAvi(Avis $avi): static
    {
        if ($this->avis->removeElement($avi)) {
            // set the owning side to null (unless already changed)
            if ($avi->getSeanceid() === $this) {
               
            }
            
        }

        return $this;
    }
}
