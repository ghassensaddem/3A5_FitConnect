<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ReservationRepository;

#[ORM\Entity(repositoryClass: ReservationRepository::class)]
#[ORM\Table(name: 'reservation')]
class Reservation
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

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?string $nbr = null;

    public function getNbr(): ?int
    {
        return $this->nbr;
    }

    public function setNbr(int $nbr): self
    {
        $this->nbr = $nbr;
        return $this;
    }

    

    #[ORM\ManyToOne(targetEntity: Activiteevent::class, inversedBy: 'activiteevents')]
    #[ORM\JoinColumn(name: 'idActiviteevent', referencedColumnName: 'id')]
    private ?Activiteevent $event = null;

    public function getEvent(): ?Event
    {
        return $this->event;
    }

    public function setEvent(?Activiteevent $event): self
    {
        $this->event = $event;
        return $this;
    }
}