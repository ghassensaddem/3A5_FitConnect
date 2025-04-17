<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity]
class Historique
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 50)]
    private string $action;

    #[ORM\Column(type: "string", length: 50)]
    private string $entite;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $date;

    #[ORM\Column(type: "text")]
    private string $details;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getAction()
    {
        return $this->action;
    }

    public function setAction($value)
    {
        $this->action = $value;
    }

    public function getEntite()
    {
        return $this->entite;
    }

    public function setEntite($value)
    {
        $this->entite = $value;
    }

    public function getDate()
    {
        return $this->date;
    }

    public function setDate($value)
    {
        $this->date = $value;
    }

    public function getDetails()
    {
        return $this->details;
    }

    public function setDetails($value)
    {
        $this->details = $value;
    }
}
