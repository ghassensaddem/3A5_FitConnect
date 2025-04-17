<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\CategorieEquipementRepository;

#[ORM\Entity(repositoryClass: CategorieEquipementRepository::class)]
#[ORM\Table(name: 'categorie_equipement')]
class CategorieEquipement
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

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nom = null;

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $description = null;

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Equipement::class, mappedBy: 'categorieEquipement')]
    private Collection $equipements;

    public function __construct()
    {
        $this->equipements = new ArrayCollection();
    }

    /**
     * @return Collection<int, Equipement>
     */
    public function getEquipements(): Collection
    {
        if (!$this->equipements instanceof Collection) {
            $this->equipements = new ArrayCollection();
        }
        return $this->equipements;
    }

    public function addEquipement(Equipement $equipement): self
    {
        if (!$this->getEquipements()->contains($equipement)) {
            $this->getEquipements()->add($equipement);
        }
        return $this;
    }

    public function removeEquipement(Equipement $equipement): self
    {
        $this->getEquipements()->removeElement($equipement);
        return $this;
    }

}
