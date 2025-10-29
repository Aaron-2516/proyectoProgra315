package MODELS;

import org.junit.*;

import java.sql.*;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class SolicitudesIT {

    private static final String IT_USER       = "it_usuario";
    private static final String IT_PASS       = "it_pass";
    private static final String IT_USER_ID    = "USR_IT";
    private static final String IT_CAT_ID     = "CAT_IT";
    private static final String IT_CAT_NOMBRE = "IT-CATEGORIA";
    private static final String IT_PRI_ID     = "PRI1";   // ajusta a tu catálogo real
    private static final String IT_EST_ID     = "EST1";   // ajusta a tu catálogo real

    private SolicitudesUserModel model;

    @BeforeClass
    public static void seedCatalogosYUsuario() throws Exception {
        System.out.println("\n[IT] ===== INICIO SEED (catálogos y usuario) =====");
        try (Connection cn = DatabaseConnection.getConnection()) {
            cn.setAutoCommit(false);

            run(cn, "INSERT INTO roles(id_rol, rol) " +
                    "SELECT '1','USUARIO' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id_rol='1')");
            System.out.println("[IT] Seed: rol '1/USUARIO' OK");

            run(cn, "INSERT INTO estados(id, nombre) " +
                    "SELECT ?, 'ABIERTA' WHERE NOT EXISTS (SELECT 1 FROM estados WHERE id=?)",
                    IT_EST_ID, IT_EST_ID);
            System.out.println("[IT] Seed: estado '" + IT_EST_ID + "/ABIERTA' OK");

            run(cn, "INSERT INTO prioridades(id, nombre) " +
                    "SELECT ?, 'Normal' WHERE NOT EXISTS (SELECT 1 FROM prioridades WHERE id=?)",
                    IT_PRI_ID, IT_PRI_ID);
            System.out.println("[IT] Seed: prioridad '" + IT_PRI_ID + "/Normal' OK");

            run(cn, "INSERT INTO categorias(id, nombre) " +
                    "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE id=?)",
                    IT_CAT_ID, IT_CAT_NOMBRE, IT_CAT_ID);
            System.out.println("[IT] Seed: categoría '" + IT_CAT_ID + "/" + IT_CAT_NOMBRE + "' OK");

            run(cn,
                "INSERT INTO usuarios " +
                " (id, usuario, contrasena, id_rol, nombre, apellidos, correo, creado_en) " +
                "SELECT ?, ?, ?, '1', ?, ?, ?, NOW() " +
                "WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = ?)",
                IT_USER_ID, IT_USER, IT_PASS,
                "USUARIO PRUEBA 1", "IT Apellido", "it.usuario@example.com",
                IT_USER_ID
            );
            System.out.println("[IT] Seed: usuario '" + IT_USER_ID + "/" + IT_USER + "' OK");

            cn.commit();
        }
        System.out.println("[IT] ===== FIN SEED =====\n");
    }

    @AfterClass
    public static void cleanupGlobal() throws Exception {
        System.out.println("\n[IT] ===== INICIO CLEANUP (solicitudes IT-*) =====");
        try (Connection cn = DatabaseConnection.getConnection()) {
            int del = runUpdate(cn,
                "DELETE FROM solicitudes WHERE descripcion LIKE 'IT-%' AND creada_por = ?",
                IT_USER_ID
            );
            System.out.println("[IT] Cleanup: solicitudes eliminadas = " + del);
        }
        System.out.println("[IT] ===== FIN CLEANUP =====\n");
    }

    private static void run(Connection cn, String sql, Object... params) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        }
    }

    private static int runUpdate(Connection cn, String sql, Object... params) throws Exception {
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            return ps.executeUpdate();
        }
    }

    @Before
    public void setUp() {
        model = new SolicitudesUserModel();
    }

    @Test
    public void listarCategorias_devuelveAlMenosUna() {
        LinkedHashMap<String, String> map = model.listarCategoriasNombreId();
        assertNotNull(map);
        System.out.println("[IT] Categorías disponibles = " + map.size());
        assertTrue("Debe haber categorías", map.size() > 0);

        boolean contieneIT = map.containsKey(IT_CAT_NOMBRE)
                && IT_CAT_ID.equals(map.get(IT_CAT_NOMBRE));
        System.out.println("[IT] Contiene categoría de pruebas (" + IT_CAT_NOMBRE + "/" + IT_CAT_ID + ") = " + contieneIT);
        assertTrue(contieneIT);
    }

    @Test
    public void crearYLeerSolicitud_ok() throws Exception {
        String creadaPorId = UsuarioModel.capturarIdUser(IT_USER, IT_PASS);
        assertEquals(IT_USER_ID, creadaPorId);
        System.out.println("[IT] Usuario autenticado para crear: " + creadaPorId);

        String desc = "IT-" + System.nanoTime();
        boolean ok = model.crearSolicitud(desc, creadaPorId, IT_CAT_ID);
        assertTrue("Debe insertar la solicitud", ok);
        System.out.println("[IT] Solicitud creada con descripción: " + desc);

        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                "SELECT s.id, s.descripcion, s.creada_por, s.categoria_id, s.estado_id, e.nombre AS estado " +
                "FROM solicitudes s JOIN estados e ON e.id = s.estado_id " +
                "WHERE s.descripcion = ? AND s.creada_por = ?")) {
            ps.setString(1, desc);
            ps.setString(2, IT_USER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue("Debe existir la fila insertada", rs.next());
                String idSol = rs.getString("id");
                String estadoNom = rs.getString("estado");
                System.out.println("[IT] Verificación OK -> ID: " + idSol +
                                   " | Estado: " + rs.getString("estado_id") + "/" + estadoNom +
                                   " | Categoría: " + rs.getString("categoria_id"));
                assertEquals(desc, rs.getString("descripcion"));
                assertEquals(IT_CAT_ID, rs.getString("categoria_id"));
                assertEquals(IT_EST_ID, rs.getString("estado_id"));
                assertEquals("ABIERTA", estadoNom);
            }
        }
    }

    @Test
    public void actualizarYEliminarSolicitud_ok() throws Exception {
        // Crear
        String userId = UsuarioModel.capturarIdUser(IT_USER, IT_PASS);
        assertEquals(IT_USER_ID, userId);
        String original = "IT-" + System.nanoTime();
        assertTrue(model.crearSolicitud(original, userId, IT_CAT_ID));
        System.out.println("[IT] Solicitud creada para UPDATE/DELETE: " + original);

        // Obtener ID
        String idCreado;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                 "SELECT id FROM solicitudes WHERE descripcion = ? AND creada_por = ?")) {
            ps.setString(1, original);
            ps.setString(2, IT_USER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                idCreado = rs.getString("id");
            }
        }
        System.out.println("[IT] ID obtenido: " + idCreado);

        // Actualizar descripción
        String nuevaDesc = original + "-UPD";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                 "UPDATE solicitudes SET descripcion=? WHERE id=? AND creada_por=?")) {
            ps.setString(1, nuevaDesc);
            ps.setString(2, idCreado);
            ps.setString(3, IT_USER_ID);
            int upd = ps.executeUpdate();
            System.out.println("[IT] UPDATE filas = " + upd + " (nueva desc: " + nuevaDesc + ")");
            assertEquals(1, upd);
        }

        // Verificar actualización
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                 "SELECT descripcion FROM solicitudes WHERE id=?")) {
            ps.setString(1, idCreado);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(nuevaDesc, rs.getString("descripcion"));
                System.out.println("[IT] Verificación UPDATE OK para id: " + idCreado);
            }
        }

        // Eliminar (cancelar)
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                 "DELETE FROM solicitudes WHERE id=? AND creada_por=?")) {
            ps.setString(1, idCreado);
            ps.setString(2, IT_USER_ID);
            int del = ps.executeUpdate();
            System.out.println("[IT] DELETE filas = " + del + " (id: " + idCreado + ")");
            assertEquals(1, del);
        }
        System.out.println("[IT] Ciclo crear->actualizar->eliminar OK para id: " + idCreado);
    }
}
