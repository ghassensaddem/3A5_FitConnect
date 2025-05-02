<?php

namespace App\Controller;
use Psr\Log\LoggerInterface;
use App\Repository\CommandeRepository;
use App\Repository\CategorieEquipementRepository;
use App\Entity\Equipement;
use App\Repository\EquipementRepository;
use Doctrine\ORM\EntityManagerInterface;
use App\Form\EquipementType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\File\UploadedFile;

final class EquipementController extends AbstractController
{
  
    #[Route('/ajouter_equipement', name: 'ajouter_equipement')]
    public function ajouter_equipement(Request $request, EntityManagerInterface $em): Response
    {
        $equipement = new Equipement();
        $form = $this->createForm(EquipementType::class, $equipement);
        
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $imageFile */
            $imageFile = $form->get('imageFile')->getData();
            
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move(
                    $this->getParameter('photo_dir'),
                    $newFilename
                );
                $equipement->setImage($newFilename);
            }
            
            $em->persist($equipement);
            $em->flush();
            
            $this->addFlash('success', 'L\'équipement a été ajouté avec succès');
            return $this->redirectToRoute('afficher_equipement');
        }

        return $this->render('equipement/ajouter_equipement.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/afficher_equipement', name: 'afficher_equipement')]
    public function afficher_equipement(
        Request $request,
        EquipementRepository $repository
    ): Response {
        $searchTerm = $request->query->get('q', '');
        $sortBy = $request->query->get('sort', 'id'); // Par défaut tri par ID
        $direction = $request->query->get('direction', 'asc'); // Par défaut ordre ascendant
    
        // Validation des paramètres de tri
        $allowedSorts = ['id', 'nom', 'prix', 'quantiteStock'];
        $sortBy = in_array($sortBy, $allowedSorts) ? $sortBy : 'id';
        $direction = in_array(strtolower($direction), ['asc', 'desc']) ? strtolower($direction) : 'asc';
    
        $queryBuilder = $repository->createQueryBuilder('e')
            ->leftJoin('e.categorie', 'c');
    
        // Ajout de la recherche
        if ($searchTerm) {
            $queryBuilder
                ->where('e.nom LIKE :term OR e.description LIKE :term OR c.nom LIKE :term')
                ->setParameter('term', '%'.$searchTerm.'%');
        }
    
        // Ajout du tri
        $queryBuilder->orderBy('e.'.$sortBy, $direction);
    
        $equipements = $queryBuilder->getQuery()->getResult();
    
        return $this->render('equipement/AfficherEquipement.html.twig', [
            'equipements' => $equipements,
            'search_term' => $searchTerm,
            'current_sort' => $sortBy,
            'current_direction' => $direction
        ]);
    }
    
 
    #[Route('/modifier_equipement/{id}', name: 'modifier_equipement')]
    public function modifier_equipement(Request $request, EntityManagerInterface $em, int $id): Response
    {
        $equipement = $em->getRepository(Equipement::class)->find($id);
        
        if (!$equipement) {
            throw $this->createNotFoundException('Équipement non trouvé');
        }
        
        $oldImage = $equipement->getImage();
        $form = $this->createForm(EquipementType::class, $equipement);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
          
            $imageFile = $form->get('imageFile')->getData();
            
            if ($imageFile) {
                if ($oldImage) {
                    $oldImagePath = $this->getParameter('photo_dir').'/'.$oldImage;
                    if (file_exists($oldImagePath)) {
                        unlink($oldImagePath);
                    }
                }
                
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move(
                    $this->getParameter('photo_dir'),
                    $newFilename
                );
                $equipement->setImage($newFilename);
            }
            
            $em->flush();
            $this->addFlash('success', 'Modification réussie');
            return $this->redirectToRoute('afficher_equipement');
        }
    
        return $this->render('equipement/modifier_equipement.html.twig', [
            'form' => $form->createView(),
            'equipement' => $equipement
        ]);
    }

    #[Route('/delete_equipement/{id}', name: 'delete_equipement')]
    public function delete(EntityManagerInterface $em, int $id): Response
    {
        $equipement = $em->getRepository(Equipement::class)->find($id);
        
        if (!$equipement) {
            throw $this->createNotFoundException('Equipement non trouvée');
        }

        // Suppression de l'image associée
        $image = $equipement->getImage();
        if ($image) {
            $imagePath = $this->getParameter('photo_dir').'/'.$image;
            if (file_exists($imagePath)) {
                unlink($imagePath);
            }
        }

        $em->remove($equipement);
        $em->flush();

        $this->addFlash('success', 'L\'équipement a été supprimé avec succès');
        return $this->redirectToRoute('afficher_equipement');
    }
}