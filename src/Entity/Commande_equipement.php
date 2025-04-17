<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Commande;

#[ORM\Entity]
class Commande_equipement
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Commande::class, inversedBy: "commande_equipements")]
    #[ORM\JoinColumn(name: 'commande_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Commande $commande_id;

    #[ORM\Column(type: "integer")]
    private int $equipement_id;

    #[ORM\Column(type: "integer")]
    private int $quantite;

    #[ORM\Column(type: "float")]
    private float $prix_unitaire;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getCommande_id()
    {
        return $this->commande_id;
    }

    public function setCommande_id($value)
    {
        $this->commande_id = $value;
    }

    public function getEquipement_id()
    {
        return $this->equipement_id;
    }

    public function setEquipement_id($value)
    {
        $this->equipement_id = $value;
    }

    public function getQuantite()
    {
        return $this->quantite;
    }

    public function setQuantite($value)
    {
        $this->quantite = $value;
    }

    public function getPrix_unitaire()
    {
        return $this->prix_unitaire;
    }

    public function setPrix_unitaire($value)
    {
        $this->prix_unitaire = $value;
    }

    public function getEquipementId(): ?int
    {
        return $this->equipement_id;
    }

    public function setEquipementId(int $equipement_id): static
    {
        $this->equipement_id = $equipement_id;

        return $this;
    }

    public function getPrixUnitaire(): ?float
    {
        return $this->prix_unitaire;
    }

    public function setPrixUnitaire(float $prix_unitaire): static
    {
        $this->prix_unitaire = $prix_unitaire;

        return $this;
    }

    public function getCommandeId(): ?Commande
    {
        return $this->commande_id;
    }

    public function setCommandeId(?Commande $commande_id): static
    {
        $this->commande_id = $commande_id;

        return $this;
    }
}
