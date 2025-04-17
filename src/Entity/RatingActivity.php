<?php

namespace App\Entity;

use App\Repository\RatingActivityRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: RatingActivityRepository::class)]
#[ORM\Table(name: 'ratingactivity')]
class RatingActivity
{
    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Activity::class)]
    #[ORM\JoinColumn(name: 'idActivity', referencedColumnName: 'idActivity', nullable: false)]
    private ?Activity $activity = null;

    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Sallesportif::class)]
    #[ORM\JoinColumn(name: 'idSalle', referencedColumnName: 'idSalle', nullable: false)]
    private ?Sallesportif $salle = null;

    #[ORM\Id]
    #[ORM\ManyToOne(targetEntity: Client::class)]
    #[ORM\JoinColumn(name: 'idClient', referencedColumnName: 'id', nullable: false)]
    private ?Client $client = null;

    #[ORM\Column(name: 'ratingStars', type: 'integer')]
    private ?int $ratingStars = null;

    #[ORM\Column(name: 'review', type: 'string', length: 250, nullable: true)]
    private ?string $review = null;

    // Getters and setters
    public function getActivity(): ?Activity
    {
        return $this->activity;
    }

    public function setActivity(?Activity $activity): self
    {
        $this->activity = $activity;
        return $this;
    }

    public function getSalle(): ?Sallesportif
    {
        return $this->salle;
    }

    public function setSalle(?Sallesportif $salle): self
    {
        $this->salle = $salle;
        return $this;
    }

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): self
    {
        $this->client = $client;
        return $this;
    }

    public function getRatingStars(): ?int
    {
        return $this->ratingStars;
    }

    public function setRatingStars(int $ratingStars): self
    {
        $this->ratingStars = $ratingStars;
        return $this;
    }

    public function getReview(): ?string
    {
        return $this->review;
    }

    public function setReview(?string $review): self
    {
        $this->review = $review;
        return $this;
    }
    
}