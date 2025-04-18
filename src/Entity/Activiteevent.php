<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ActiviteeventRepository;

#[ORM\Entity(repositoryClass: ActiviteeventRepository::class)]
#[ORM\Table(name: 'activiteevent')]
class Activiteevent
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

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $nbrparticipant = null;

    public function getNbrparticipant(): ?int
    {
        return $this->nbrparticipant;
    }

    public function setNbrparticipant(int $nbrparticipant): self
    {
        $this->nbrparticipant = $nbrparticipant;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Event::class, inversedBy: 'activiteevents')]
    #[ORM\JoinColumn(name: 'idEvent', referencedColumnName: 'id')]
    private ?Event $event = null;

    public function getEvent(): ?Event
    {
        return $this->event;
    }

    public function setEvent(?Event $event): self
    {
        $this->event = $event;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Typeactivite::class, inversedBy: 'activiteevents')]
    #[ORM\JoinColumn(name: 'idTypeActivite', referencedColumnName: 'id')]
    private ?Typeactivite $typeactivite = null;

    public function getTypeactivite(): ?Typeactivite
    {
        return $this->typeactivite;
    }

    public function setTypeactivite(?Typeactivite $typeactivite): self
    {
        $this->typeactivite = $typeactivite;
        return $this;
    }
    public function __toString(): string
    {
        $output = 'Activité #'.$this->getId();
        
        if (!empty($this->horaire)) {
            $output .= ' - '.$this->horaire;
        }
        
        if ($this->nbrparticipant > 0) {  // Correction: nbrparticipant au lieu de nbpparticipant
            $output .= ' ('.$this->nbrparticipant.' participants)';
        }
        
        // Utilisation des objets relationnels plutôt que des IDs directs
        if ($this->event !== null) {
            $output .= ' - Evt:'.$this->event->getId();
        }
        
        if ($this->typeactivite !== null) {
            $output .= ' - Type:'.$this->typeactivite->getId();
        }
        
        return $output;
    }
}
