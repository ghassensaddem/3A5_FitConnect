<?php

namespace App\Repository;

use App\Entity\Sallesportif;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class SallesportifRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Sallesportif::class);
    }

    // Add custom methods as needed
}