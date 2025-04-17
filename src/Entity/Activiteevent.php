<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Typeactivite;

#[ORM\Entity]
class Activiteevent
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 30)]
    private string $horaire;

    #[ORM\Column(type: "integer")]
    private int $nbrparticipant;

        #[ORM\ManyToOne(targetEntity: Event::class, inversedBy: "activiteevents")]
    #[ORM\JoinColumn(name: 'idEvent', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Event $idEvent;

        #[ORM\ManyToOne(targetEntity: Typeactivite::class, inversedBy: "activiteevents")]
    #[ORM\JoinColumn(name: 'idTypeActivite', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Typeactivite $idTypeActivite;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getHoraire()
    {
        return $this->horaire;
    }

    public function setHoraire($value)
    {
        $this->horaire = $value;
    }

    public function getNbrparticipant()
    {
        return $this->nbrparticipant;
    }

    public function setNbrparticipant($value)
    {
        $this->nbrparticipant = $value;
    }

    public function getIdEvent()
    {
        return $this->idEvent;
    }

    public function setIdEvent($value)
    {
        $this->idEvent = $value;
    }

    public function getIdTypeActivite()
    {
        return $this->idTypeActivite;
    }

    public function setIdTypeActivite($value)
    {
        $this->idTypeActivite = $value;
    }
}
