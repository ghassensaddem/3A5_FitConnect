<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use App\Entity\Seance;

#[ORM\Entity]
class Avis
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Seance::class, inversedBy: "aviss")]
    #[ORM\JoinColumn(name: 'seanceid', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Seance $seanceid;

    #[ORM\Column(type: "string", length: 100)]
    private string $commentaire;

    #[ORM\Column(type: "integer")]
    private int $note;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getSeanceid()
    {
        return $this->seanceid;
    }

    public function setSeanceid($value)
    {
        $this->seanceid = $value;
    }

    public function getCommentaire()
    {
        return $this->commentaire;
    }

    public function setCommentaire($value)
    {
        $this->commentaire = $value;
    }

    public function getNote()
    {
        return $this->note;
    }

    public function setNote($value)
    {
        $this->note = $value;
    }
}
