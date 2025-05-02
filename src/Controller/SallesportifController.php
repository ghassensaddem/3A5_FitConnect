<?php

namespace App\Controller;

use App\Entity\Sallesportif;
use App\Form\SallesportifType;
use App\Repository\SallesportifRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


#[Route('/sallesportif')]
class SallesportifController extends AbstractController
{
    #[Route('/', name: 'app_sallesportif_index', methods: ['GET'])]
    public function index(SallesportifRepository $sallesportifRepository): Response
    {
        return $this->render('sallesportif/index.html.twig', [
            'sallesportifs' => $sallesportifRepository->findAll(),
        ]);
    }

    // Dans SallesportifController.php

#[Route('/new', name: 'app_sallesportif_new', methods: ['GET', 'POST'])]
public function new(Request $request, EntityManagerInterface $entityManager): Response
{
    $sallesportif = new Sallesportif();
    $form = $this->createForm(SallesportifType::class, $sallesportif);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Validation supplémentaire
        if (!in_array($sallesportif->getAddresseSalle(), Sallesportif::ADDRESSES)) {
            $this->addFlash('error', 'Adresse non valide');
            return $this->redirectToRoute('app_sallesportif_new');
        }
        
        $entityManager->persist($sallesportif);
        $entityManager->flush();

        return $this->redirectToRoute('app_sallesportif_index', [], Response::HTTP_SEE_OTHER);
    }

    return $this->render('sallesportif/new.html.twig', [
        'sallesportif' => $sallesportif,
        'form' => $form,
        'addresses' => Sallesportif::ADDRESSES // Pour utilisation dans le template si besoin
    ]);
}
    #[Route('/{idSalle}', name: 'app_sallesportif_show', methods: ['GET'])]
    public function show(Sallesportif $sallesportif): Response
    {
        return $this->render('sallesportif/show.html.twig', [
            'sallesportif' => $sallesportif,
        ]);
    }

    #[Route('/{idSalle}/edit', name: 'app_sallesportif_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Sallesportif $sallesportif, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(SallesportifType::class, $sallesportif);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_sallesportif_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('sallesportif/edit.html.twig', [
            'sallesportif' => $sallesportif,
            'form' => $form,
        ]);
    }

    #[Route('/{idSalle}', name: 'app_sallesportif_delete', methods: ['POST'])]
    public function delete(Request $request, Sallesportif $sallesportif, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$sallesportif->getIdSalle(), $request->request->get('_token'))) {
            $entityManager->remove($sallesportif);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_sallesportif_index', [], Response::HTTP_SEE_OTHER);
    }
}