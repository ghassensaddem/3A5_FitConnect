<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Ratingactivity;

#[ORM\Entity]
class Sallesportif
{

    #[ORM\Id]
    #[ORM\Column(type: "integer", name: "idSalle")]
    private int $idSalle;

    #[ORM\Column(type: "string", length: 30, name: "nomSalle")]
    private string $nomSalle;

    #[ORM\Column(type: "string", length: 250, name: "addresseSalle")]
    private string $addresseSalle;

    #[ORM\Column(type: "string", name: "HoraireOuverture")]
    private string $HoraireOuverture;

    #[ORM\Column(type: "string", name: "HoraireFermeture")]
    private string $HoraireFermeture;

    #[ORM\Column(type: "integer")]
    private int $Capacity;

    public function getIdSalle()
    {
        return $this->idSalle;
    }

    public function setIdSalle($value)
    {
        $this->idSalle = $value;
    }

    public function getNomSalle()
    {
        return $this->nomSalle;
    }

    public function setNomSalle($value)
    {
        $this->nomSalle = $value;
    }

    public function getAddresseSalle()
    {
        return $this->addresseSalle;
    }

    public function setAddresseSalle($value)
    {
        $this->addresseSalle = $value;
    }

    public function getHoraireOuverture()
    {
        return $this->HoraireOuverture;
    }

    public function setHoraireOuverture($value)
    {
        $this->HoraireOuverture = $value;
    }

    public function getHoraireFermeture()
    {
        return $this->HoraireFermeture;
    }

    public function setHoraireFermeture($value)
    {
        $this->HoraireFermeture = $value;
    }

    public function getCapacity()
    {
        return $this->Capacity;
    }

    public function setCapacity($value)
    {
        $this->Capacity = $value;
    }

    #[ORM\OneToMany(mappedBy: "idSalle", targetEntity: Planningactivity::class)]
    private Collection $planningactivitys;

        public function getPlanningactivitys(): Collection
        {
            return $this->planningactivitys;
        }
    
        public function addPlanningactivity(Planningactivity $planningactivity): self
        {
            if (!$this->planningactivitys->contains($planningactivity)) {
                $this->planningactivitys[] = $planningactivity;
                $planningactivity->setIdSalle($this);
            }
    
            return $this;
        }
    
        public function removePlanningactivity(Planningactivity $planningactivity): self
        {
            if ($this->planningactivitys->removeElement($planningactivity)) {
                // set the owning side to null (unless already changed)
                if ($planningactivity->getIdSalle() === $this) {
                    $planningactivity->setIdSalle(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "idSalle", targetEntity: Ratingactivity::class)]
    private Collection $ratingactivitys;

    public function __construct()
    {
        $this->planningactivitys = new ArrayCollection();
        $this->ratingactivitys = new ArrayCollection();
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
            $ratingactivity->setIdSalle($this);
        }

        return $this;
    }

    public function removeRatingactivity(Ratingactivity $ratingactivity): static
    {
        if ($this->ratingactivitys->removeElement($ratingactivity)) {
            // set the owning side to null (unless already changed)
            if ($ratingactivity->getIdSalle() === $this) {
                $ratingactivity->setIdSalle(null);
            }
        }

        return $this;
    }
}
