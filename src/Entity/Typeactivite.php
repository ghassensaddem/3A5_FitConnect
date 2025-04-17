<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Activiteevent;

#[ORM\Entity]
class Typeactivite
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 30)]
    private string $title;

    #[ORM\Column(type: "string", length: 250)]
    private string $description;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getTitle()
    {
        return $this->title;
    }

    public function setTitle($value)
    {
        $this->title = $value;
    }

    public function getDescription()
    {
        return $this->description;
    }

    public function setDescription($value)
    {
        $this->description = $value;
    }

    #[ORM\OneToMany(mappedBy: "idTypeActivite", targetEntity: Activiteevent::class)]
    private Collection $activiteevents;

    public function __construct()
    {
        $this->activiteevents = new ArrayCollection();
    }

        public function getActiviteevents(): Collection
        {
            return $this->activiteevents;
        }
    
        public function addActiviteevent(Activiteevent $activiteevent): self
        {
            if (!$this->activiteevents->contains($activiteevent)) {
                $this->activiteevents[] = $activiteevent;
                $activiteevent->setIdTypeActivite($this);
            }
    
            return $this;
        }
    
        public function removeActiviteevent(Activiteevent $activiteevent): self
        {
            if ($this->activiteevents->removeElement($activiteevent)) {
                // set the owning side to null (unless already changed)
                if ($activiteevent->getIdTypeActivite() === $this) {
                    $activiteevent->setIdTypeActivite(null);
                }
            }
    
            return $this;
        }
}
