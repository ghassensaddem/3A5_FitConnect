<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class PageHomeController extends AbstractController
{
    #[Route('/page/home', name: 'app_page_home')]
    public function index(): Response
    {
        return $this->render('front.html.twig', [
            'controller_name' => 'PageHomeController',
        ]);
    }
}
