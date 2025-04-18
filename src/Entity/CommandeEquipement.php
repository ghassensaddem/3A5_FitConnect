<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\CommandeEquipementRepository;

#[ORM\Entity(repositoryClass: CommandeEquipementRepository::class)]
#[ORM\Table(name: 'commande_equipement')]
class CommandeEquipement
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

    #[ORM\ManyToOne(targetEntity: Commande::class, inversedBy: 'commandeEquipements')]
    #[ORM\JoinColumn(name: 'commande_id', referencedColumnName: 'id')]
    private ?Commande $commande = null;

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): self
    {
        $this->commande = $commande;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $equipement_id = null;

    public function getEquipement_id(): ?int
    {
        return $this->equipement_id;
    }

    public function setEquipement_id(int $equipement_id): self
    {
        $this->equipement_id = $equipement_id;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $quantite = null;

    public function getQuantite(): ?int
    {
        return $this->quantite;
    }

    public function setQuantite(int $quantite): self
    {
        $this->quantite = $quantite;
        return $this;
    }

    #[ORM\Column(type: 'decimal', nullable: false)]
    private ?float $prix_unitaire = null;

    public function getPrix_unitaire(): ?float
    {
        return $this->prix_unitaire;
    }

    public function setPrix_unitaire(float $prix_unitaire): self
    {
        $this->prix_unitaire = $prix_unitaire;
        return $this;
    }

    public function getEquipementId(): ?int
    {
        return $this->equipement_id;
    }

    public function setEquipementId(int $equipement_id): self
    {
        $this->equipement_id = $equipement_id;

        return $this;
    }

    public function getPrixUnitaire(): ?string
    {
        return $this->prix_unitaire;
    }

    public function setPrixUnitaire(string $prix_unitaire): self
    {
        $this->prix_unitaire = $prix_unitaire;

        return $this;
    }

}
