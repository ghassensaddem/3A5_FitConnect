<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Activity;

#[ORM\Entity]
class Categorieactivity
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $idCategorie;

    #[ORM\Column(type: "string", length: 30)]
    private string $nomCategorie;

    #[ORM\Column(type: "string", length: 250)]
    private string $description;

    public function getIdCategorie()
    {
        return $this->idCategorie;
    }

    public function setIdCategorie($value)
    {
        $this->idCategorie = $value;
    }

    public function getNomCategorie()
    {
        return $this->nomCategorie;
    }

    public function setNomCategorie($value)
    {
        $this->nomCategorie = $value;
    }

    public function getDescription()
    {
        return $this->description;
    }

    public function setDescription($value)
    {
        $this->description = $value;
    }

    #[ORM\OneToMany(mappedBy: "categorieActivity", targetEntity: Activity::class)]
    private Collection $activitys;

    public function __construct()
    {
        $this->activitys = new ArrayCollection();
    }

        public function getActivitys(): Collection
        {
            return $this->activitys;
        }
    
        public function addActivity(Activity $activity): self
        {
            if (!$this->activitys->contains($activity)) {
                $this->activitys[] = $activity;
                $activity->setCategorieActivity($this);
            }
    
            return $this;
        }
    
        public function removeActivity(Activity $activity): self
        {
            if ($this->activitys->removeElement($activity)) {
                // set the owning side to null (unless already changed)
                if ($activity->getCategorieActivity() === $this) {
                    $activity->setCategorieActivity(null);
                }
            }
    
            return $this;
        }
}
