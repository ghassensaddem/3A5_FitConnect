<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;

#[ORM\Entity]
class Application
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $dateDebut;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $dateFin;

        #[ORM\ManyToOne(targetEntity: Programme::class, inversedBy: "applications")]
    #[ORM\JoinColumn(name: 'idProgramme', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Programme $idProgramme;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "applications")]
    #[ORM\JoinColumn(name: 'idClient', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $idClient;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getDateDebut()
    {
        return $this->dateDebut;
    }

    public function setDateDebut($value)
    {
        $this->dateDebut = $value;
    }

    public function getDateFin()
    {
        return $this->dateFin;
    }

    public function setDateFin($value)
    {
        $this->dateFin = $value;
    }

    public function getIdProgramme()
    {
        return $this->idProgramme;
    }

    public function setIdProgramme($value)
    {
        $this->idProgramme = $value;
    }

    public function getIdClient()
    {
        return $this->idClient;
    }

    public function setIdClient($value)
    {
        $this->idClient = $value;
    }
}
