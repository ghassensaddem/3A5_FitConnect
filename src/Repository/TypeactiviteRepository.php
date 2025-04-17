<?php

namespace App\Repository;

use App\Entity\Typeactivite;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class TypeactiviteRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Typeactivite::class);
    }

    // Add custom methods as needed
}