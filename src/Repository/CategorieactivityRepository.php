<?php

namespace App\Repository;

use App\Entity\Categorieactivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CategorieactivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Categorieactivity::class);
    }

    // Add custom methods as needed
}