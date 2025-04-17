<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;

#[ORM\Entity]
class Poids
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "date")]
    private \DateTimeInterface $date;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "poidss")]
    #[ORM\JoinColumn(name: 'ClientId', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $ClientId;

    #[ORM\Column(type: "integer")]
    private int $valeur;

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

    public function getClientId()
    {
        return $this->ClientId;
    }

    public function setClientId($value)
    {
        $this->ClientId = $value;
    }

    public function getValeur()
    {
        return $this->valeur;
    }

    public function setValeur($value)
    {
        $this->valeur = $value;
    }
}
