package com.Multi.tenant_SaaS_Project_Management_System.Entities;

import com.Multi.tenant_SaaS_Project_Management_System.Enums.ConfigType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_configurations",
        uniqueConstraints = @UniqueConstraint(name = "unique_tenant_config", columnNames = {"tenant_id", "config_key"}))

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;  // raw FK, no Tenant entity reference

    @Column(name = "config_key", nullable = false, length = 100)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "config_type", nullable = false)
    @Builder.Default
    private ConfigType configType = ConfigType.STRING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Object getParsedValue() {
        if (configValue == null) return null;

        switch (configType) {
            case NUMBER:
                return parseNumber(configValue);
            case BOOLEAN:
                return parseBoolean(configValue);
            case JSON:
                return configValue; // Just return raw string, parse elsewhere if needed
            default:
                return configValue;
        }
    }

    private Object parseNumber(String value) {
        try {
            return extracted(value);
        } catch (NumberFormatException e) {
            return value; // fallback to original string if parse fails
        }
    }

    private static double extracted(String value) {
        if (value.contains(".")) {
            return Double.parseDouble(value);
        } else {
            return Long.parseLong(value);
        }
    }

    private Boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }
}
