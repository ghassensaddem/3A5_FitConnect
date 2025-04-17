<?php

namespace App\Repository;

use App\Entity\Admin;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class AdminRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Admin::class);
    }

    // Add custom methods as needed
    public function ajouter(Admin $admin): void
    {
        $em = $this->getEntityManager();
        $em->persist($admin);
        $em->flush();
    }

    public function supprimer(Admin $admin): void
    {
        $em = $this->getEntityManager();
        $em->remove($admin);
        $em->flush();
    }

    public function modifier(Admin $admin): void
    {
        $em = $this->getEntityManager();
        $em->flush();
    }

    public function afficher(): array
    {
        return $this->findAll();
    }

    public function updateImage(Admin $admin): void
    {
        $queryBuilder = $this->createQueryBuilder('a')
            ->update(Admin::class, 'a')
            ->set('a.image', ':image')
            ->where('a.id = :id')
            ->setParameter('image', $admin->getImage())
            ->setParameter('id', $admin->getId());
        $queryBuilder->getQuery()->execute();
    }

   
    public function setRole(Admin $admin): void
    {
        $queryBuilder = $this->createQueryBuilder('a')
            ->update(Admin::class, 'a')
            ->set('a.role', ':role')
            ->where('a.id = :id')
            ->setParameter('role', $admin->getRole())
            ->setParameter('id', $admin->getId());
        $queryBuilder->getQuery()->execute();
    }

    
    
}