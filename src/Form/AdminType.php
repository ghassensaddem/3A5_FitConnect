<?php
namespace App\Form;

use App\Entity\Admin;
use App\Repository\ActivityRepository;
use App\Repository\SallesportifRepository;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;

class AdminType extends AbstractType
{

    

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        
    $builder
    ->add('nom', null, [
        'label' => 'Nom',
        'required' => false,
        'attr' => [
            'class' => 'form-control',
            'placeholder' => 'Entrez le nom',
           
        ],
    ])
    ->add('prenom', null, [
        'label' => 'Prénom',
        'required' => false,
        'attr' => [
            'class' => 'form-control',
            'placeholder' => 'Entrez le prénom',
            
        ],
    ])
    ->add('sexe', ChoiceType::class, [
        'label' => 'Sexe',
        'choices' => [
            'Homme' => 'Homme',
            'Femme' => 'Femme',
        ],
        'attr' => [
            'class' => 'form-control',
            
        ],
    ])
    ->add('mdp', null, [
        'label' => 'Mot de passe',
        'required' => false,
        'attr' => [
            'class' => 'form-control',
            'placeholder' => 'Entrez le mot de passe',

        ],
    ])
    ->add('dateNaissance', DateType::class, [
        'label' => 'Date de naissance',
        'required' => false,
        'mapped' => false,
        'years' => range(1900, date('Y')),
        'attr' => [
            'class' => 'form-control',
            
        ],
    ])
    ->add('email', null, [
        'label' => 'Email',
        'required' => false,
        'attr' => [
            'class' => 'form-control',
            'placeholder' => 'Entrez l\'email',
            
        ],
    ])
    ->add('role', ChoiceType::class, [
        'label' => 'Rôle',
        'choices' => [
            'admin' => 'admin',
            'supadmin' => 'supadmin',
        ],
        'attr' => [
            'class' => 'form-control',
            
        ],
    ])
    ->add('image', FileType::class, [
        'label' => 'Photo de profil',
        'mapped' => false,
        'required' => false,
        'attr' => [
            'class' => 'form-control',
            
        ],
    ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Admin::class,
        ]);
    }

   
}