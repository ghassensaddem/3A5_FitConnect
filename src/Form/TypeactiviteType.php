<?php

namespace App\Form;

use App\Entity\Typeactivite;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\Regex;

class TypeactiviteType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('title', null, [
                'constraints' => [
                    new NotBlank([
                        'message' => 'Le champ title est obligatoire.'
                    ]),
                    new Length([
                        'min' => 7,
                        'max' => 20,
                        'minMessage' => 'Le titre doit contenir au moins {{ limit }} caractères.',
                        'maxMessage' => 'Le titre ne peut pas dépasser {{ limit }} caractères.'
                    ]),
                    new Regex([
                        'pattern' => '/^[a-zA-Z ]+$/', // Lettres et espace uniquement
                        'message' => 'Le titre ne peut contenir que des lettres et des espaces.'
                    ])
                ]
            ])
            ->add('description', null, [
                'constraints' => [
                    new NotBlank([
                        'message' => 'Le champ description est obligatoire.'
                    ]),
                    new Length([
                        'min' => 7,
                        'max' => 20,
                        'minMessage' => 'La description doit contenir au moins {{ limit }} caractères.',
                        'maxMessage' => 'La description ne peut pas dépasser {{ limit }} caractères.'
                    ]),
                    new Regex([
                        'pattern' => '/^[a-zA-Z ]+$/', // Lettres et espace uniquement
                        'message' => 'La description ne peut contenir que des lettres et des espaces.'
                    ])
                ]
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Typeactivite::class,
        ]);
    }
}
