<?php

namespace App\Repository;

use App\Entity\Planningactivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class PlanningactivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Planningactivity::class);
    }

    // Add custom methods as needed
}