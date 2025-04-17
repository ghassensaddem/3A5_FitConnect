<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Sallesportif;

#[ORM\Entity]
class Planningactivity
{

    #[ORM\Id]
        #[ORM\ManyToOne(targetEntity: Activity::class, inversedBy: "planningactivitys")]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity', onDelete: 'CASCADE')]
    private Activity $idActivity;

    #[ORM\Id]
        #[ORM\ManyToOne(targetEntity: Sallesportif::class, inversedBy: "planningactivitys")]
    #[ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle', onDelete: 'CASCADE')]
    private Sallesportif $idSalle;

    #[ORM\Column(type: "integer")]
    private int $capacityMax;

    #[ORM\Column(type: "integer")]
    private int $nombreinscription;

    public function getIdActivity()
    {
        return $this->idActivity;
    }

    public function setIdActivity($value)
    {
        $this->idActivity = $value;
    }

    public function getIdSalle()
    {
        return $this->idSalle;
    }

    public function setIdSalle($value)
    {
        $this->idSalle = $value;
    }

    public function getCapacityMax()
    {
        return $this->capacityMax;
    }

    public function setCapacityMax($value)
    {
        $this->capacityMax = $value;
    }

    public function getNombreinscription()
    {
        return $this->nombreinscription;
    }

    public function setNombreinscription($value)
    {
        $this->nombreinscription = $value;
    }
}
