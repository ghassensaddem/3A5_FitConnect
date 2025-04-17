<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Equipement;

#[ORM\Entity]
class Categorie_equipement
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 255)]
    private string $nom;

    #[ORM\Column(type: "string", length: 255)]
    private string $description;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
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

    #[ORM\OneToMany(mappedBy: "categorie_id", targetEntity: Equipement::class)]
    private Collection $equipements;

    public function __construct()
    {
        $this->equipements = new ArrayCollection();
    }

        public function getEquipements(): Collection
        {
            return $this->equipements;
        }
    
        public function addEquipement(Equipement $equipement): self
        {
            if (!$this->equipements->contains($equipement)) {
                $this->equipements[] = $equipement;
                $equipement->setCategorie_id($this);
            }
    
            return $this;
        }
    
        public function removeEquipement(Equipement $equipement): self
        {
            if ($this->equipements->removeElement($equipement)) {
                // set the owning side to null (unless already changed)
                if ($equipement->getCategorie_id() === $this) {
                    $equipement->setCategorie_id(null);
                }
            }
    
            return $this;
        }
}
