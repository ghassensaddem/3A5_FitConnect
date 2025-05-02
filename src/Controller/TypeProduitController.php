<?php

namespace App\Controller;

use App\Entity\CategorieEquipement;
use App\Form\CategorieEquipementType;
use App\Repository\CategorieEquipementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class TypeProduitController extends AbstractController
{
    #[Route('/ajouter_type_produit', name: 'ajouter_type_produit')]
    public function ajouter(Request $request, EntityManagerInterface $em): Response
    {
        $categorie = new CategorieEquipement();
        $form = $this->createForm(CategorieEquipementType::class, $categorie);
        
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($categorie);
            $em->flush();
            return $this->redirectToRoute('afficher_type_produit');
        }

        return $this->render('type_produit/ajouter_type_equipement.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/modifier_type_produit/{id}', name: 'modifier_type_produit')]
    public function modifier(Request $request, EntityManagerInterface $em, int $id): Response
    {
        $categorie = $em->getRepository(CategorieEquipement::class)->find($id);
        
        if (!$categorie) {
            throw $this->createNotFoundException('Catégorie non trouvée');
        }

        $form = $this->createForm(CategorieEquipementType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->flush();
            return $this->redirectToRoute('afficher_type_produit');
        }

        return $this->render('type_produit/ModifierTypeEquipement.html.twig', [
            'form' => $form->createView(),
           
        ]);
    }

    #[Route('/delete_categorie/{id}', name: 'delete_categorie')]
    public function delete(EntityManagerInterface $em, int $id): Response
    {
        $categorie = $em->getRepository(CategorieEquipement::class)->find($id);
        
        if (!$categorie) {
            throw $this->createNotFoundException('Catégorie non trouvée');
        }

        $em->remove($categorie);
        $em->flush();

        return $this->redirectToRoute('afficher_type_produit');
    }

    #[Route('/afficher_type_produit', name: 'afficher_type_produit')]
    public function afficher(Request $request, CategorieEquipementRepository $repository): Response
    {
        $searchTerm = $request->query->get('q', '');
        
        $categories = $repository->createQueryBuilder('c')
            ->where('c.nom LIKE :term OR c.description LIKE :term')
            ->setParameter('term', '%'.$searchTerm.'%')
            ->getQuery()
            ->getResult();
    
        return $this->render('type_produit/AfficherTypeEquipement.html.twig', [
            'categories' => $categories,
            'search_term' => $searchTerm
        ]);
    }
}