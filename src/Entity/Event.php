<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

use Doctrine\Common\Collections\Collection;
use App\Entity\Activiteevent;

#[ORM\Entity]
class Event
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 30)]
    private string $date;

    #[ORM\Column(type: "float")]
    private float $prixdupass;

    #[ORM\Column(type: "string", length: 250)]
    private string $lieu;

    #[ORM\Column(type: "string", length: 20)]
    private string $horaire;

    #[ORM\Column(type: "string", length: 255)]
    private string $image;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getDate()
    {
        return $this->date;
    }

    public function setDate($value)
    {
        $this->date = $value;
    }

    public function getPrixdupass()
    {
        return $this->prixdupass;
    }

    public function setPrixdupass($value)
    {
        $this->prixdupass = $value;
    }

    public function getLieu()
    {
        return $this->lieu;
    }

    public function setLieu($value)
    {
        $this->lieu = $value;
    }

    public function getHoraire()
    {
        return $this->horaire;
    }

    public function setHoraire($value)
    {
        $this->horaire = $value;
    }

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    #[ORM\OneToMany(mappedBy: "idEvent", targetEntity: Client::class)]
    private Collection $clients;

        public function getClients(): Collection
        {
            return $this->clients;
        }
    
        public function addClient(Client $client): self
        {
            if (!$this->clients->contains($client)) {
                $this->clients[] = $client;
                $client->setIdEvent($this);
            }
    
            return $this;
        }
    
        public function removeClient(Client $client): self
        {
            if ($this->clients->removeElement($client)) {
                // set the owning side to null (unless already changed)
                if ($client->getIdEvent() === $this) {
                    $client->setIdEvent(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "idEvent", targetEntity: Activiteevent::class)]
    private Collection $activiteevents;

    public function __construct()
    {
        $this->clients = new ArrayCollection();
        $this->activiteevents = new ArrayCollection();
    }

    /**
     * @return Collection<int, Activiteevent>
     */
    public function getActiviteevents(): Collection
    {
        return $this->activiteevents;
    }

    public function addActiviteevent(Activiteevent $activiteevent): static
    {
        if (!$this->activiteevents->contains($activiteevent)) {
            $this->activiteevents->add($activiteevent);
            $activiteevent->setIdEvent($this);
        }

        return $this;
    }

    public function removeActiviteevent(Activiteevent $activiteevent): static
    {
        if ($this->activiteevents->removeElement($activiteevent)) {
            // set the owning side to null (unless already changed)
            if ($activiteevent->getIdEvent() === $this) {
                $activiteevent->setIdEvent(null);
            }
        }

        return $this;
    }
}
