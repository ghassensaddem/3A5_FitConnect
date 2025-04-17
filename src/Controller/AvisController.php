<?php

namespace App\Controller;
use App\Entity\Seance; // Assure-toi que cette ligne est présente
use App\Entity\Client;
use App\Entity\Avis;
use App\Form\AvisType;
use App\Repository\AvisRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Core\Security; // n'oublie pas d'ajouter ce use si nécessaire
 // n'oublie pas d'ajouter ce use si nécessaire
use Symfony\Component\Validator\Validator\ValidatorInterface; // <-- Add this use statement




#[Route('/avis')]
final class AvisController extends AbstractController
{
    #[Route(name: 'app_avis_index', methods: ['GET'])]
    public function index(AvisRepository $avisRepository): Response
{
    $avis = $avisRepository->findAll();
    $somme = 0;
    $nombre = count($avis);

    foreach ($avis as $avi) {
        $somme += $avi->getNote();
    }

    $moyenne = $nombre > 0 ? $somme / $nombre : 0;

    return $this->render('avis/index.html.twig', [
        'avis' => $avis,
        'moyenne' => $moyenne,
    ]);
}

    #[Route('/back', name: 'app_avis_back_index', methods: ['GET'])]
    public function backIndex(AvisRepository $avisRepository): Response
    {
        return $this->render('avis/back.html.twig', [
            'avis' => $avisRepository->findAll(),
        ]);
    }
    #[Route('/new', name: 'app_avis_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, Security $security, ValidatorInterface $validator): Response
    {
        $seanceId = $request->query->get('id');
        $seance = $entityManager->getRepository(Seance::class)->find($seanceId);
        
        if (!$seance) {
            throw $this->createNotFoundException('Séance non trouvée.');
        }
    
        // Récupérer l'entité Client avec un ID fixe (par exemple, ID=1)
        $client = $security->getUser();

        if (!$client instanceof Client) {
            // TEMPORAIRE : utiliser un client "fixe" pour test
            $client = $entityManager->getRepository(Client::class)->find(1);
        }
    
        $avi = new Avis();
        $avi->setClient($client);
        $avi->setSeanceid($seance);
    
        $form = $this->createForm(AvisType::class, $avi);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $violations = $validator->validate($avi);
            if (count($violations) > 0) {
                // Gérer les erreurs de validation
                return $this->render('avis/new.html.twig', [
                    'form' => $form->createView(),
                    'errors' => $violations,
                ]);
            }
    
            $entityManager->persist($avi);
            $entityManager->flush();
    
            return $this->redirectToRoute('app_avis_index');
        }
    
        return $this->render('avis/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/edit', name: 'app_avis_edit', methods: ['GET', 'POST'])]
public function edit(Request $request, Avis $avi, EntityManagerInterface $entityManager, Security $security): Response
{
    // Vérifier que l'avis appartient au client connecté
    $client = $security->getUser();

    if (!$client instanceof Client) {
        $client = $entityManager->getRepository(Client::class)->find(1);
    }
    if ($avi->getClient() !== $client) {
        throw $this->createAccessDeniedException('Vous ne pouvez modifier que vos propres avis.');
    }
        

    $form = $this->createForm(AvisType::class, $avi);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager->flush();
        return $this->redirectToRoute('app_avis_index');
    }

    return $this->render('avis/edit.html.twig', [
        'avi' => $avi,
        'form' => $form->createView(),
    ]);
}

#[Route('/{id}', name: 'app_avis_delete', methods: ['POST'])]
public function delete(Request $request, Avis $avi, EntityManagerInterface $entityManager, Security $security): Response
{
    // Vérifier que l'avis appartient au client connecté
    $client = $security->getUser();

    if (!$client instanceof Client) {
        $client = $entityManager->getRepository(Client::class)->find(1);
    }
    

    if ($this->isCsrfTokenValid('delete' . $avi->getId(), $request->request->get('_token'))) {
        $entityManager->remove($avi);
        $entityManager->flush();
    }

    return $this->redirectToRoute('app_avis_index');
}

    
}    