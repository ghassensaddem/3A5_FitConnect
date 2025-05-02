<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Categorieactivity;
use Doctrine\Common\Collections\Collection;
use App\Entity\Ratingactivity;

#[ORM\Entity]
class Activity
{

    #[ORM\Id]
    #[ORM\Column(type: "integer", name: "idActivity")]
    private int $idActivity;

    #[ORM\Column(type: "string", length: 20, name: "nomActivity")]
    private string $nomActivity;

    #[ORM\Column(type: "string", length: 250)]
    private string $IconActivity;

        #[ORM\ManyToOne(targetEntity: Categorieactivity::class, inversedBy: "activitys")]
    #[ORM\JoinColumn(name: 'categorieActivity', referencedColumnName: 'idCategorie', onDelete: 'CASCADE')]
    private Categorieactivity $categorieActivity;

    public function getIdActivity()
    {
        return $this->idActivity;
    }

    public function setIdActivity($value)
    {
        $this->idActivity = $value;
    }

    public function getNomActivity()
    {
        return $this->nomActivity;
    }

    public function setNomActivity($value)
    {
        $this->nomActivity = $value;
    }

    public function getIconActivity()
    {
        return $this->IconActivity;
    }

    public function setIconActivity($value)
    {
        $this->IconActivity = $value;
    }

    public function getCategorieActivity()
    {
        return $this->categorieActivity;
    }

    public function setCategorieActivity($value)
    {
        $this->categorieActivity = $value;
    }

    #[ORM\OneToMany(mappedBy: "idActivity", targetEntity: Planningactivity::class)]
    private Collection $planningactivitys;

        public function getPlanningactivitys(): Collection
        {
            return $this->planningactivitys;
        }
    
        public function addPlanningactivity(Planningactivity $planningactivity): self
        {
            if (!$this->planningactivitys->contains($planningactivity)) {
                $this->planningactivitys[] = $planningactivity;
                $planningactivity->setIdActivity($this);
            }
    
            return $this;
        }
    
        public function removePlanningactivity(Planningactivity $planningactivity): self
        {
            if ($this->planningactivitys->removeElement($planningactivity)) {
                // set the owning side to null (unless already changed)
                if ($planningactivity->getIdActivity() === $this) {
                    $planningactivity->setIdActivity(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "idActivity", targetEntity: Seance::class)]
    private Collection $seances;

    #[ORM\OneToMany(mappedBy: "idActivity", targetEntity: Ratingactivity::class)]
    private Collection $ratingactivitys;

    public function __construct()
    {
        $this->planningactivitys = new ArrayCollection();
        $this->seances = new ArrayCollection();
        $this->ratingactivitys = new ArrayCollection();
    }

    /**
     * @return Collection<int, Seance>
     */
    public function getSeances(): Collection
    {
        return $this->seances;
    }

    public function addSeance(Seance $seance): static
    {
        if (!$this->seances->contains($seance)) {
            $this->seances->add($seance);
            $seance->setIdActivity($this);
        }

        return $this;
    }

    public function removeSeance(Seance $seance): static
    {
        if ($this->seances->removeElement($seance)) {
            // set the owning side to null (unless already changed)
            if ($seance->getIdActivity() === $this) {
                $seance->setIdActivity(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Ratingactivity>
     */
    public function getRatingactivitys(): Collection
    {
        return $this->ratingactivitys;
    }

    public function addRatingactivity(Ratingactivity $ratingactivity): static
    {
        if (!$this->ratingactivitys->contains($ratingactivity)) {
            $this->ratingactivitys->add($ratingactivity);
            $ratingactivity->setIdActivity($this);
        }

        return $this;
    }

    public function removeRatingactivity(Ratingactivity $ratingactivity): static
    {
        if ($this->ratingactivitys->removeElement($ratingactivity)) {
            // set the owning side to null (unless already changed)
            if ($ratingactivity->getIdActivity() === $this) {
                $ratingactivity->setIdActivity(null);
            }
        }

        return $this;
    }
}
