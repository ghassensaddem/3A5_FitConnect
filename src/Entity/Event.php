<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\EventRepository;

#[ORM\Entity(repositoryClass: EventRepository::class)]
#[ORM\Table(name: 'event')]
class Event
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
    private ?string $date = null;

    public function getDate(): ?string
    {
        return $this->date;
    }

    public function setDate(string $date): self
    {
        $this->date = $date;
        return $this;
    }

    #[ORM\Column(type: 'float', nullable: false)]
    private ?float $prixdupass = null;

    public function getPrixdupass(): ?float
    {
        return $this->prixdupass;
    }

    public function setPrixdupass(float $prixdupass): self
    {
        $this->prixdupass = $prixdupass;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: true)]
    private ?string $lieu = null;

    public function getLieu(): ?string
    {
        return $this->lieu;
    }

    public function setLieu(?string $lieu): self
    {
        $this->lieu = $lieu;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $horaire = null;

    public function getHoraire(): ?string
    {
        return $this->horaire;
    }

    public function setHoraire(string $horaire): self
    {
        $this->horaire = $horaire;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false,length:255)]
    private ?string $image = null;

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(string $image): self
    {
        $this->image = $image;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Activiteevent::class, mappedBy: 'event')]
    private Collection $activiteevents;

    /**
     * @return Collection<int, Activiteevent>
     */
    public function getActiviteevents(): Collection
    {
        if (!$this->activiteevents instanceof Collection) {
            $this->activiteevents = new ArrayCollection();
        }
        return $this->activiteevents;
    }

    public function addActiviteevent(Activiteevent $activiteevent): self
    {
        if (!$this->getActiviteevents()->contains($activiteevent)) {
            $this->getActiviteevents()->add($activiteevent);
        }
        return $this;
    }

    public function removeActiviteevent(Activiteevent $activiteevent): self
    {
        $this->getActiviteevents()->removeElement($activiteevent);
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Client::class, mappedBy: 'event')]
    private Collection $clients;

    public function __construct()
    {
        $this->activiteevents = new ArrayCollection();
        $this->clients = new ArrayCollection();
    }

    /**
     * @return Collection<int, Client>
     */
    public function getClients(): Collection
    {
        if (!$this->clients instanceof Collection) {
            $this->clients = new ArrayCollection();
        }
        return $this->clients;
    }

    public function addClient(Client $client): self
    {
        if (!$this->getClients()->contains($client)) {
            $this->getClients()->add($client);
        }
        return $this;
    }

    public function removeClient(Client $client): self
    {
        $this->getClients()->removeElement($client);
        return $this;
    }
    public function __toString(): string
    {
        try {
            // Vérification plus robuste
            $dateStr = (method_exists($this, 'getDate') && $this->getDate() instanceof \DateTimeInterface) 
                ? $this->getDate()
                : null;
    
            return $dateStr ? $dateStr.' - Événement #'.$this->getId() 
                          : 'Événement #'.$this->getId();
            
        } catch (\Exception $e) {
            // Fallback sécurisé en cas d'erreur
            return 'Événement #'.$this->getId();
        }
    }
}
