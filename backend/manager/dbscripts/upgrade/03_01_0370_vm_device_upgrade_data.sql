----------------------------------
-- VM Device helper functions
----------------------------------

Create or replace FUNCTION update_vm_video_cards_03_01_0370(v_vm_id UUID, v_old_num_of_monitors int,
                                                            v_new_num_of_monitors int, v_default_display_type int)
RETURNS void
AS $function$
DECLARE
    v_display_type varchar(10);
    v_display_mem  varchar(10);
BEGIN
   if (v_new_num_of_monitors != v_old_num_of_monitors) then
      -- set device type : vnc/spice
      if (v_default_display_type = 0) then
         v_display_type := 'cirrus';
      else
         v_display_type := 'qxl';
      end if;
      if (v_new_num_of_monitors > v_old_num_of_monitors) then

           -- set device memmory according to number of monitors
           if (v_new_num_of_monitors <= 2) then
               v_display_mem := 'vram=65536';
            else
               v_display_mem := 'vram=32768';
           end if;

           for i in 1..v_new_num_of_monitors loop
               insert INTO vm_device(
               device_id, vm_id,type,device,address,boot_order,spec_params,is_managed,is_plugged,is_readonly)
               values ( uuid_generate_v1(), v_vm_id, 'video', v_display_type, '', null, v_display_mem, true, null, false);
           end loop;

      else
          delete from vm_device where device_id in(
              select device_id from vm_device where vm_id = v_vm_id and type = 'video' and device = v_display_type
               LIMIT (v_old_num_of_monitors - v_new_num_of_monitors));
      end if;
   end if;
END; $function$
LANGUAGE plpgsql;

-- sets a device boot order , if multiple devices found all are taking in account
CREATE OR REPLACE FUNCTION set_vm_device_boot_order_03_01_0370(v_vm_id UUID, v_type varchar(30), v_device varchar(30))
RETURNS void
AS $function$
DECLARE
    v_sql text := 'SELECT device_id FROM vm_device where vm_id=''' || v_vm_id || ''' and type=''' || v_type || ''' and device=''' ||  v_device || '''';
    v_cur refcursor ;
    v_boot_order int;
    v_device_id UUID;
    v_last_boot_order int;
BEGIN

    v_last_boot_order := max(boot_order) from vm_device where vm_id = v_vm_id;
    if (v_last_boot_order  IS NULL) then
       v_boot_order := 1;
    else
       v_boot_order := v_last_boot_order + 1;
    end if;
    BEGIN
        OPEN v_cur for EXECUTE v_sql;
        LOOP
            FETCH v_cur INTO v_device_id;
            EXIT WHEN NOT FOUND;
            update vm_device set boot_order = v_boot_order where vm_id = v_vm_id and device_id = v_device_id;
            v_boot_order := v_boot_order + 1;
        END LOOP;
        CLOSE v_cur;
    END;

END; $function$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION set_vm_devices_boot_order_03_01_0370(v_vm_id UUID)
RETURNS void
AS $function$
DECLARE
    v_old_boot_order int;
BEGIN
    v_old_boot_order := default_boot_sequence from vm_static where vm_guid = v_vm_id;
    -- reset boot order before re computing it
    update vm_device set boot_order = null where vm_id = v_vm_id;
    case v_old_boot_order
        when 0 then  -- C
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
        when 1 then  -- DC
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
        when 2 then  -- N
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
        when 3 then  -- CDN
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
        when 4 then  -- CND
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
        when 5 then  -- DCN
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
        when 6 then  -- DNC
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
        when 7 then  -- NCD
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
        when 8 then  -- NDC
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
       when 9 then  -- CD
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
        when 10 then -- D
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
        when 11 then -- CN
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
        when 12 then -- DN
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
        when 13 then -- NC
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'disk');
        when 14 then -- ND
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'interface', 'bridge');
            perform set_vm_device_boot_order_03_01_0370(v_vm_id, 'disk', 'cdrom');
      end case;
END; $function$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION vm_device_upgrade_data_03_01_0370()
RETURNS void
AS $function$
DECLARE
    v_cur CURSOR FOR SELECT * FROM vm_static;
    v_record vm_static%ROWTYPE;
BEGIN
   IF EXISTS (SELECT * FROM information_schema.tables WHERE table_name ILIKE 'vm_device') THEN

      truncate table vm_device;
       ALTER TABLE vm_device ALTER COLUMN is_plugged DROP not null;
       -- insert images (disks) to vm_device
       insert INTO vm_device(
       device_id, vm_id, type, device, address, boot_order, spec_params, is_managed, is_plugged, is_readonly)
       select image_id, vm_id, 'disk', 'disk', '', null, '', true, true, false from image_vm_map
       where active = true ;

       -- insert network interfaces to vm_device
       insert INTO vm_device(
       device_id, vm_id, type, device, address, boot_order, spec_params, is_managed, is_plugged, is_readonly)
       select id, vm_guid, 'interface', 'bridge', '', null, '', true, true, false  from vm_interface
       where vm_guid IS NOT NULL;

       insert INTO vm_device(
       device_id, vm_id, type, device, address, boot_order, spec_params, is_managed, is_plugged, is_readonly)
       select id, vmt_guid, 'interface', 'bridge', '', null, '', true, true, false  from vm_interface
       where vmt_guid IS NOT NULL;

       -- insert CDROM to vm_device (only 1 is supported currently)
       insert INTO vm_device(
       device_id, vm_id, type, device, address, boot_order, spec_params, is_managed, is_plugged, is_readonly)
       select uuid_generate_v1(), vm_guid, 'disk', 'cdrom', '', null, 'path=' || iso_path, true , null, false
       from vm_static where iso_path != '';

       OPEN v_cur;
       LOOP
           FETCH v_cur INTO v_record;
           EXIT WHEN NOT FOUND;
           -- insert Video Cards to vm_device (according to number of monitors)
           perform  update_vm_video_cards_03_01_0370(v_record.vm_guid, 0, v_record.num_of_monitors, v_record.default_display_type);
           -- set boot order
           perform set_vm_devices_boot_order_03_01_0370(v_record.vm_guid);
       END LOOP;
       CLOSE v_cur;
       -- update device id in spec_params
       update vm_device set spec_params = (case spec_params
                                           when  '' then 'deviceId=' || device_id
                                           else  spec_params || ',deviceId=' || device_id
                                           end);
   END IF;
END; $function$
LANGUAGE plpgsql;

--This script has a fix , therfor call is commented
--SELECT * FROM vm_device_upgrade_data_03_01_0370();


DROP FUNCTION vm_device_upgrade_data_03_01_0370();
DROP FUNCTION update_vm_video_cards_03_01_0370(v_vm_id UUID, v_old_num_of_monitors int, v_new_num_of_monitors int, v_default_display_type int);
DROP FUNCTION set_vm_device_boot_order_03_01_0370(v_vm_id UUID, v_type varchar(30), v_device varchar(30));
DROP FUNCTION set_vm_devices_boot_order_03_01_0370(v_vm_id UUID);


