<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Coach;
use Doctrine\Common\Collections\Collection;
use App\Entity\Seance;

#[ORM\Entity]
class Programme
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Coach::class, inversedBy: "programmes")]
    #[ORM\JoinColumn(name: 'coach_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Coach $coach_id;

    #[ORM\Column(type: "float")]
    private float $prix;

    #[ORM\Column(type: "string", length: 250)]
    private string $description;

    #[ORM\Column(type: "string", length: 150)]
    private string $lieu;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getCoach_id()
    {
        return $this->coach_id;
    }

    public function setCoach_id($value)
    {
        $this->coach_id = $value;
    }

    public function getPrix()
    {
        return $this->prix;
    }

    public function setPrix($value)
    {
        $this->prix = $value;
    }

    public function getDescription()
    {
        return $this->description;
    }

    public function setDescription($value)
    {
        $this->description = $value;
    }

    public function getLieu()
    {
        return $this->lieu;
    }

    public function setLieu($value)
    {
        $this->lieu = $value;
    }

    #[ORM\OneToMany(mappedBy: "idProgramme", targetEntity: Application::class)]
    private Collection $applications;

        public function getApplications(): Collection
        {
            return $this->applications;
        }
    
        public function addApplication(Application $application): self
        {
            if (!$this->applications->contains($application)) {
                $this->applications[] = $application;
                $application->setIdProgramme($this);
            }
    
            return $this;
        }
    
        public function removeApplication(Application $application): self
        {
            if ($this->applications->removeElement($application)) {
                // set the owning side to null (unless already changed)
                if ($application->getIdProgramme() === $this) {
                    $application->setIdProgramme(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "programme_id", targetEntity: Seance::class)]
    private Collection $seances;

    public function __construct()
    {
        $this->applications = new ArrayCollection();
        $this->seances = new ArrayCollection();
    }

        public function getSeances(): Collection
        {
            return $this->seances;
        }
    
        public function addSeance(Seance $seance): self
        {
            if (!$this->seances->contains($seance)) {
                $this->seances[] = $seance;
                $seance->setProgramme_id($this);
            }
    
            return $this;
        }
    
        public function removeSeance(Seance $seance): self
        {
            if ($this->seances->removeElement($seance)) {
                // set the owning side to null (unless already changed)
                if ($seance->getProgramme_id() === $this) {
                    $seance->setProgramme_id(null);
                }
            }
    
            return $this;
        }

        public function getCoachId(): ?Coach
        {
            return $this->coach_id;
        }

        public function setCoachId(?Coach $coach_id): static
        {
            $this->coach_id = $coach_id;

            return $this;
        }
}
