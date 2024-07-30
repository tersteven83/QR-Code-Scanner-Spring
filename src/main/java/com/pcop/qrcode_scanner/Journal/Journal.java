package com.pcop.qrcode_scanner.Journal;


import com.pcop.qrcode_scanner.Etudiant.Etudiant;
import com.pcop.qrcode_scanner.Operateur.Operateur;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Journal {
    @Id
    @SequenceGenerator(
            name = "journal_id_seq",
            sequenceName = "journal_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "journal_id_seq"
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(
            name = "operation",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String operation;
    
    @ManyToOne(
            optional = false,
            cascade = CascadeType.ALL, 
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_operateur", 
            referencedColumnName = "id"
    )
    private Operateur operateur;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;
    
    public Journal() {
    }
    
    public Journal(String operation, Operateur operateur, LocalDateTime operationDate) {
        this.operation = operation;
        this.operateur = operateur;
        this.operationDate = operationDate;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", operation='" + operation + '\'' +
                ", operateur=" + operateur +
                ", operationDate=" + operationDate +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Operateur getOperateur() {
        return operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }
}
