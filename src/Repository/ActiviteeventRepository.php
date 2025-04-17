<?php

namespace App\Repository;

use App\Entity\Activiteevent;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class ActiviteeventRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Activiteevent::class);
    }

    // Add custom methods as needed
}