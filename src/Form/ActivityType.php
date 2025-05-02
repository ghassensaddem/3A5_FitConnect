<?php

namespace App\Form;

use App\Entity\Activity;
use App\Entity\CategorieActivity;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;

class ActivityType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('nomActivity', TextType::class, [
                'label' => 'Nom de l\'activité',
                'attr' => [
                    'class' => 'form-control',
                    'maxlength' => 20
                ]
            ])
            ->add('imageFile', FileType::class, [
                'label' => 'Icône de l\'activité',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '2M',
                        'mimeTypes' => [
                            'image/jpeg',
                            'image/png',
                            'image/webp',
                        ],
                        'mimeTypesMessage' => 'Veuillez uploader une image valide (JPG, PNG ou WEBP)',
                    ])
                ],
                'attr' => [
                    'class' => 'form-control-file',
                    'accept' => 'image/jpeg,image/png,image/webp'
                ]
            ])
            ->add('categorieActivity', EntityType::class, [
                'class' => CategorieActivity::class,
                'choice_label' => 'nomCategorie',
                'label' => 'Catégorie',
                'attr' => [
                    'class' => 'form-control'
                ]
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Activity::class,
        ]);
    }
}