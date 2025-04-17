<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Categorie_equipement;

#[ORM\Entity]
class Equipement
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Categorie_equipement::class, inversedBy: "equipements")]
    #[ORM\JoinColumn(name: 'categorie_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Categorie_equipement $categorie_id;

    #[ORM\Column(type: "string", length: 255)]
    private string $nom;

    #[ORM\Column(type: "string", length: 255)]
    private string $description;

    #[ORM\Column(type: "float")]
    private float $prix;

    #[ORM\Column(type: "integer")]
    private int $quantite_stock;

    #[ORM\Column(type: "string", length: 255)]
    private string $image;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getCategorie_id()
    {
        return $this->categorie_id;
    }

    public function setCategorie_id($value)
    {
        $this->categorie_id = $value;
    }

    public function getNom()
    {
        return $this->nom;
    }

    public function setNom($value)
    {
        $this->nom = $value;
    }

    public function getDescription()
    {
        return $this->description;
    }

    public function setDescription($value)
    {
        $this->description = $value;
    }

    public function getPrix()
    {
        return $this->prix;
    }

    public function setPrix($value)
    {
        $this->prix = $value;
    }

    public function getQuantite_stock()
    {
        return $this->quantite_stock;
    }

    public function setQuantite_stock($value)
    {
        $this->quantite_stock = $value;
    }

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    public function getQuantiteStock(): ?int
    {
        return $this->quantite_stock;
    }

    public function setQuantiteStock(int $quantite_stock): static
    {
        $this->quantite_stock = $quantite_stock;

        return $this;
    }

    public function getCategorieId(): ?Categorie_equipement
    {
        return $this->categorie_id;
    }

    public function setCategorieId(?Categorie_equipement $categorie_id): static
    {
        $this->categorie_id = $categorie_id;

        return $this;
    }
}
