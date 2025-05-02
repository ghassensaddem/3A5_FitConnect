<?php

namespace App\Entity;

use App\Repository\EquipementRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EquipementRepository::class)]

class Equipement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'string', length: 255)]
    #[Assert\NotBlank(message: "Le nom ne peut pas être vide")]
    #[Assert\Length(
        min: 2,
        max: 255,
        minMessage: "Le nom doit contenir au moins {{ limit }} caractères",
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères"
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-ZÀ-ÿ\s\-']+$/u",
        message: "Le nom contient des caractères non autorisés"
    )]
    private ?string $nom = null;

    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    #[Assert\NotBlank(message: "Le description ne peut pas être vide")]
    #[Assert\Length(
        max: 255,
        maxMessage: "La description ne peut pas dépasser {{ limit }} caractères"
    )]
    private ?string $description=null;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
#[Assert\NotBlank(message: "Le prix ne peut pas être vide")]
#[Assert\Positive(message: "Le prix doit être positif")]
#[Assert\LessThan(value: 1000000, message: "Le prix ne peut pas dépasser 1 000 000")]
private ?string $prix ; 
#[ORM\Column(type: 'integer')]
#[Assert\NotBlank(message: "La quantité ne peut pas être vide")]
#[Assert\PositiveOrZero(message: "La quantité doit être positive ou nulle")]
private ?int $quantiteStock = null;

    #[ORM\ManyToOne(targetEntity: CategorieEquipement::class)]
    #[ORM\JoinColumn(name: 'categorie_id', onDelete: 'SET NULL')]
    #[Assert\NotNull(message: "Veuillez sélectionner une catégorie")]
    private ?CategorieEquipement $categorie = null;
    #[ORM\Column(type: 'string', length: 255, nullable: true)]
  
    private ?string $image = null;








    // Getters et Setters

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): string
    {
        return $this->nom;
    }

    public function setNom(?string $nom): self
    {
        $this->nom = $nom ? trim($nom) : null;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): self
    {
        $this->description = $description ? trim($description) : null;;
        return $this;
    }

    public function getPrix(): string
    {
        return $this->prix;
    }

    public function setPrix(?string $prix): self
    {
     
        if ($prix === null || $prix === '') {
            $this->prix = null;
        } else {
            $prix = str_replace(',', '.', $prix);
            $this->prix = number_format((float)$prix, 2, '.', '');
        }
        return $this;
    }

    public function getQuantiteStock(): int
    {
        return $this->quantiteStock;
    }

    public function setQuantiteStock(?int $quantiteStock): self
    {
        $this->quantiteStock = $quantiteStock ?? 0; // Convertit null en 0
        return $this;
    }

    public function getCategorie(): ?CategorieEquipement
    {
        return $this->categorie;
    }

    public function setCategorie(?CategorieEquipement $categorie): static
    {
        $this->categorie = $categorie;
        return $this;
    }
    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;
        return $this;
    }


    
}