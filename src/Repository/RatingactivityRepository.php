<?php

namespace App\Repository;

use App\Entity\Ratingactivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class RatingactivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Ratingactivity::class);
    }

    // Add custom methods as needed
}