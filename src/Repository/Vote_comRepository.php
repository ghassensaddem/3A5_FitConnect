<?php

namespace App\Repository;

use App\Entity\Vote_com;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class Vote_comRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Vote_com::class);
    }

    // Add custom methods as needed
}