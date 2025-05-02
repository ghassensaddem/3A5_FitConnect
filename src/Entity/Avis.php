<?php

namespace App\Entity;

use App\Repository\AvisRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: AvisRepository::class)]
class Avis
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le commentaire ne doit pas être vide.")]
    #[Assert\Length(
        min: 5,
        minMessage: "Le commentaire doit contenir au moins 5 caractères."
    )]
    private ?string $commentaire =  null;
  // Valeur par défaut vide, mais non nullable

    #[ORM\Column]
    #[Assert\NotNull(message: "La note est requise.")]
    #[Assert\Range(
        min: 0,
        max: 10,
        notInRangeMessage: "La note doit être comprise entre 0 et 10."
    )]
    private ?int $note = null;  // Note non nullable, valeur par défaut de 0

    #[ORM\ManyToOne(inversedBy: 'avis')]
#[ORM\JoinColumn(nullable: false, name: 'seanceId', referencedColumnName: 'id', onDelete: 'CASCADE')]
private ?Seance $seanceid = null;



    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCommentaire(): ?string
    {
        return $this->commentaire;
    }

    public function setCommentaire(string $commentaire): static
    {
        $this->commentaire = $commentaire;

        return $this;
    }

    public function getNote(): int
    {
        return $this->note;
    }

    public function setNote(int $note): static
    {
        $this->note = $note;

        return $this;
    }

    public function getSeanceid(): ?Seance
    {
        return $this->seanceid;
    }

    public function setSeanceid(Seance $seanceid): static
    {
        $this->seanceid = $seanceid;

        return $this;
    }

    
}
