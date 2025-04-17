<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;

#[ORM\Entity]
class Ratingactivity
{

    #[ORM\Id]
        #[ORM\ManyToOne(targetEntity: Activity::class, inversedBy: "ratingactivitys")]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity', onDelete: 'CASCADE')]
    private Activity $idActivity;

    #[ORM\Id]
        #[ORM\ManyToOne(targetEntity: Sallesportif::class, inversedBy: "ratingactivitys")]
    #[ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle', onDelete: 'CASCADE')]
    private Sallesportif $idSalle;

    #[ORM\Column(type: "integer")]
    private int $ratingStars;

    #[ORM\Column(type: "string", length: 250)]
    private string $review;

    #[ORM\Id]
        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "ratingactivitys")]
    #[ORM\JoinColumn(name: 'idClient', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $idClient;

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

    public function getRatingStars()
    {
        return $this->ratingStars;
    }

    public function setRatingStars($value)
    {
        $this->ratingStars = $value;
    }

    public function getReview()
    {
        return $this->review;
    }

    public function setReview($value)
    {
        $this->review = $value;
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
