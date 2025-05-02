<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Programme;
use Symfony\Component\Validator\Constraints as Assert;


#[ORM\Entity]
class Coach
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 10)]
    #[Assert\NotBlank(message: "Le nom ne peut pas être vide.")]
    #[Assert\Length(
        max: 10,
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    private $nom;

    #[ORM\Column(type: "string", length: 10)]
    #[Assert\NotBlank(message: "Le prénom ne peut pas être vide.")]
    #[Assert\Length(
        max: 10,
        maxMessage: "Le prénom ne peut pas dépasser {{ limit }} caractères."
    )]
    private $prenom;

    #[ORM\Column(type: "string", length: 10)]
    #[Assert\NotBlank(message: "Le sexe ne peut pas être vide.")]
    #[Assert\Choice(
        choices: ["Homme", "Femme"],
        message: "Le sexe doit être 'Homme' ou 'Femme'."
    )]
    private $sexe;

    #[ORM\Column(type: "string", length: 30)]
    #[Assert\NotBlank(message: "Le mot de passe ne peut pas être vide.")]
    #[Assert\Length(
        min: 6,
        max: 30,
        minMessage: "Le mot de passe doit comporter au moins {{ limit }} caractères.",
        maxMessage: "Le mot de passe ne peut pas dépasser {{ limit }} caractères."
    )]
    private $mdp;

    #[ORM\Column(type: "string", length: 20, name: "dateNaissance")]
    #[Assert\NotBlank(message: "La date de naissance ne peut pas être vide.")]
    #[Assert\Date(message: "La date de naissance doit être une date valide.")]
    private $dateNaissance;

    #[ORM\Column(type: "string", length: 20)]
    #[Assert\NotBlank(message: "L'email ne peut pas être vide.")]
    #[Assert\Email(message: "L'email doit être valide.")]
    private $email;

    #[ORM\Column(type: "string", length: 50, name: "lieuEngagement")]
    #[Assert\NotBlank(message: "Le lieu d'engagement ne peut pas être vide.")]
    #[Assert\Length(
        max: 50,
        maxMessage: "Le lieu d'engagement ne peut pas dépasser {{ limit }} caractères."
    )]
    private $lieuEngagement;

    #[ORM\Column(type: "string", length: 20)]
    #[Assert\NotBlank(message: "La spécialité ne peut pas être vide.")]
    #[Assert\Length(
        max: 20,
        maxMessage: "La spécialité ne peut pas dépasser {{ limit }} caractères."
    )]
    private $specialite;

    #[ORM\Column(type: "string", length: 250)]
    private $image;

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

    public function getPrenom()
    {
        return $this->prenom;
    }

    public function setPrenom($value)
    {
        $this->prenom = $value;
    }

    public function getSexe()
    {
        return $this->sexe;
    }

    public function setSexe($value)
    {
        $this->sexe = $value;
    }

    public function getMdp()
    {
        return $this->mdp;
    }

    public function setMdp($value)
    {
        $this->mdp = $value;
    }

    public function getDateNaissance()
    {
        return $this->dateNaissance;
    }

    public function setDateNaissance($value)
    {
        $this->dateNaissance = $value;
    }

    public function getEmail()
    {
        return $this->email;
    }

    public function setEmail($value)
    {
        $this->email = $value;
    }

    public function getLieuEngagement()
    {
        return $this->lieuEngagement;
    }

    public function setLieuEngagement($value)
    {
        $this->lieuEngagement = $value;
    }

    public function getSpecialite()
    {
        return $this->specialite;
    }

    public function setSpecialite($value)
    {
        $this->specialite = $value;
    }

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    #[ORM\OneToMany(mappedBy: "coach_id", targetEntity: Programme::class)]
    private Collection $programmes;

    public function __construct()
    {
        $this->programmes = new ArrayCollection();
    }

        public function getProgrammes(): Collection
        {
            return $this->programmes;
        }
    
        public function addProgramme(Programme $programme): self
        {
            if (!$this->programmes->contains($programme)) {
                $this->programmes[] = $programme;
                $programme->setCoach_id($this);
            }
    
            return $this;
        }
    
        public function removeProgramme(Programme $programme): self
        {
            if ($this->programmes->removeElement($programme)) {
                // set the owning side to null (unless already changed)
                if ($programme->getCoach_id() === $this) {
                    $programme->setCoach_id(null);
                }
            }
    
            return $this;
        }
}
