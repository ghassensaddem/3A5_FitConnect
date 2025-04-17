<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use Symfony\Component\Validator\Constraints as Assert;


#[ORM\Entity]
class Admin
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 30)]
    #[Assert\NotBlank(message: "Le nom est obligatoire.")]
    #[Assert\Length(
        max: 30,
        maxMessage: "Le nom ne doit pas dépasser {{ limit }} caractères."
    )]
    private  $nom;

    #[ORM\Column(type: "string", length: 40)]
    #[Assert\NotBlank(message: "Le prénom est obligatoire.")]
    #[Assert\Length(
        max: 40,
        maxMessage: "Le prénom ne doit pas dépasser {{ limit }} caractères."
    )]
    private  $prenom;

    #[ORM\Column(type: "string", length: 10)]
    #[Assert\NotBlank(message: "Le sexe est obligatoire.")]
    #[Assert\Choice(
        choices: ["Homme", "Femme"],
        message: "Le sexe doit être 'Homme' ou 'Femme'."
    )]
    private  $sexe;

    #[ORM\Column(type: "string", length: 32)]
    #[Assert\NotBlank(message: "Le mot de passe est obligatoire.")]
    #[Assert\Length(
        min: 6,
        minMessage: "Le mot de passe doit contenir au moins {{ limit }} caractères."
    )]
    private  $mdp;

    #[ORM\Column(type: "string", length: 20, name: "dateNaissance")]
    private  $dateNaissance;

    #[ORM\Column(type: "string", length: 30)]
    #[Assert\NotBlank(message: "L'adresse email est obligatoire.")]
    #[Assert\Email(message: "L'adresse email '{{ value }}' n'est pas valide.")]
    private  $email;

    #[ORM\Column(type: "string", length: 250)]
    private  $image;

    #[ORM\Column(type: "string", length: 50)]
    #[Assert\NotBlank(message: "Le rôle est obligatoire.")]
    #[Assert\Choice(
        choices: ["admin", "supadmin"],
        message: "Le rôle doit être 'admin' ou 'supadmin'."
    )]
    private  $role;

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

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    public function getRole()
    {
        return $this->role;
    }

    public function setRole($value)
    {
        $this->role = $value;
    }
}
