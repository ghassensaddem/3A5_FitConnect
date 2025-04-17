<?php

namespace App\Entity;

use App\Repository\EventRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: EventRepository::class)]
class Event
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private ?int $id = null;

    #[ORM\Column(type: "string", length: 30)]
    private ?string $date = null;

    #[ORM\Column(type: "float")]
    private ?float $prixdupass = null;

    #[ORM\Column(type: "string", length: 250)]
    private ?string $lieu = null;

    #[ORM\Column(type: "string", length: 20)]
    private ?string $horaire = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDate(): ?string
    {
        return $this->date;
    }

    public function setDate(string $date): static
    {
        $this->date = $date;

        return $this;
    }

    public function getPrixdupass(): ?float
    {
        return $this->prixdupass;
    }

    public function setPrixdupass(float $prixdupass): static
    {
        $this->prixdupass = $prixdupass;

        return $this;
    }

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(string $lieu): static
    {
        $this->lieu = $lieu;

        return $this;
    }

    public function getHoraire(): ?string
    {
        return $this->horaire;
    }

    public function setHoraire(string $horaire): static
    {
        $this->horaire = $horaire;

        return $this;
    }
}
