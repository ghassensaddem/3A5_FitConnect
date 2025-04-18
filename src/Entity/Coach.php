<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\CoachRepository;

#[ORM\Entity(repositoryClass: CoachRepository::class)]
#[ORM\Table(name: 'coach')]
class Coach
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
    private ?string $prenom = null;

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): self
    {
        $this->prenom = $prenom;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $sexe = null;

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(string $sexe): self
    {
        $this->sexe = $sexe;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $mdp = null;

    public function getMdp(): ?string
    {
        return $this->mdp;
    }

    public function setMdp(string $mdp): self
    {
        $this->mdp = $mdp;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $dateNaissance = null;

    public function getDateNaissance(): ?string
    {
        return $this->dateNaissance;
    }

    public function setDateNaissance(string $dateNaissance): self
    {
        $this->dateNaissance = $dateNaissance;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $email = null;

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $lieuEngagement = null;

    public function getLieuEngagement(): ?string
    {
        return $this->lieuEngagement;
    }

    public function setLieuEngagement(string $lieuEngagement): self
    {
        $this->lieuEngagement = $lieuEngagement;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $specialite = null;

    public function getSpecialite(): ?string
    {
        return $this->specialite;
    }

    public function setSpecialite(string $specialite): self
    {
        $this->specialite = $specialite;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Programme::class, mappedBy: 'coach')]
    private Collection $programmes;

    public function __construct()
    {
        $this->programmes = new ArrayCollection();
    }

    /**
     * @return Collection<int, Programme>
     */
    public function getProgrammes(): Collection
    {
        if (!$this->programmes instanceof Collection) {
            $this->programmes = new ArrayCollection();
        }
        return $this->programmes;
    }

    public function addProgramme(Programme $programme): self
    {
        if (!$this->getProgrammes()->contains($programme)) {
            $this->getProgrammes()->add($programme);
        }
        return $this;
    }

    public function removeProgramme(Programme $programme): self
    {
        $this->getProgrammes()->removeElement($programme);
        return $this;
    }

}
